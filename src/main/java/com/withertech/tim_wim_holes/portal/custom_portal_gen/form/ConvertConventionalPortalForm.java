package com.withertech.tim_wim_holes.portal.custom_portal_gen.form;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import com.withertech.tim_wim_holes.Helper;
import com.withertech.tim_wim_holes.my_util.IntBox;
import com.withertech.tim_wim_holes.portal.custom_portal_gen.CustomPortalGeneration;
import com.withertech.tim_wim_holes.portal.custom_portal_gen.PortalGenInfo;
import com.withertech.tim_wim_holes.portal.custom_portal_gen.SimpleBlockPredicate;
import com.withertech.tim_wim_holes.portal.nether_portal.*;
import net.minecraft.block.BlockState;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.ServerPlayerEntity;
import net.minecraft.util.Direction;
import net.minecraft.util.RegistryKey;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.text.TranslationTextComponent;
import net.minecraft.world.World;
import net.minecraft.world.server.ServerWorld;

import javax.annotation.Nullable;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class ConvertConventionalPortalForm extends PortalGenForm
{

	public static final Codec<ConvertConventionalPortalForm> codec = RecordCodecBuilder.create(instance ->
	{
		return instance.group(
				SimpleBlockPredicate.codec.fieldOf("portal_block").forGetter(o -> o.portalBlock)
		).apply(instance, instance.stable(ConvertConventionalPortalForm::new));
	});

	public final SimpleBlockPredicate portalBlock;

	public ConvertConventionalPortalForm(SimpleBlockPredicate portalBlock)
	{
		this.portalBlock = portalBlock;
	}

	@Deprecated
	@Nullable
	public static IntBox findBlockBoxArea(
			World world, BlockPos pos, Predicate<BlockState> predicate
	)
	{
		BlockPos startingPos = findBlockAround(world, pos, predicate);

		if (startingPos == null)
		{
			return null;
		}

		IntBox result = Helper.expandBoxArea(
				startingPos,
				p -> predicate.test(world.getBlockState(p))
		);

		if (result.getSize().equals(new BlockPos(1, 1, 1)))
		{
			return null;
		}

		return result;
	}

	@Nullable
	public static BlockPos findBlockAround(
			World world, BlockPos pos, Predicate<BlockState> predicate
	)
	{
		BlockState blockState = world.getBlockState(pos);
		if (predicate.test(blockState))
		{
			return pos;
		}

		return BlockTraverse.searchInBox(
				new IntBox(pos.add(-2, -2, -2), pos.add(2, 2, 2)),
				p ->
				{
					if (predicate.test(world.getBlockState(p)))
					{
						return p;
					}
					return null;
				}
		);
	}

	@Deprecated
	@Nullable
	public static BlockPortalShape convertToPortalShape(IntBox box)
	{
		BlockPos size = box.getSize();
		Direction.Axis axis = null;
		if (size.getX() == 1)
		{
			axis = Direction.Axis.X;
		} else if (size.getY() == 1)
		{
			axis = Direction.Axis.Y;
		} else if (size.getZ() == 1)
		{
			axis = Direction.Axis.Z;
		} else
		{
			Helper.error("The box is not flat " + box);
			return null;
		}

		return new BlockPortalShape(
				box.stream().collect(Collectors.toSet()),
				axis
		);
	}

	@Nullable
	public static PortalGenInfo tryToMatch(
			RegistryKey<World> fromDim, RegistryKey<World> toDim,
			BlockPortalShape a, BlockPortalShape b
	)
	{
		List<DiligentMatcher.TransformedShape> matchableShapeVariants =
				DiligentMatcher.getMatchableShapeVariants(
						a, 20
				);

		for (DiligentMatcher.TransformedShape variant : matchableShapeVariants)
		{
			BlockPortalShape variantMoved = variant.transformedShape.getShapeWithMovedAnchor(b.anchor);
			if (variantMoved.equals(b))
			{
				return new PortalGenInfo(
						fromDim, toDim,
						a, b,
						variant.rotation.toQuaternion(),
						variant.scale
				);
			}
		}

		return null;
	}

	@Override
	public Codec<? extends PortalGenForm> getCodec()
	{
		return codec;
	}

	@Override
	public PortalGenForm getReverse()
	{
		return this;
	}

	@Override
	public boolean perform(
			CustomPortalGeneration cpg, ServerWorld fromWorld,
			BlockPos startingPos, ServerWorld toWorld,
			@Nullable Entity triggeringEntity
	)
	{
		if (triggeringEntity == null)
		{
			Helper.error("Null triggering entity for portal conversion");
			return false;
		}

		if (!(triggeringEntity instanceof ServerPlayerEntity))
		{
			Helper.error("Non player entity triggers portal conversion");
			return false;
		}

		ServerPlayerEntity player = (ServerPlayerEntity) triggeringEntity;


		if (player.world != toWorld)
		{
			Helper.error("The player is not in the correct world " +
					player.world.getDimensionKey().getLocation());
			return false;
		}

		BlockPos playerCurrentPos = player.getPosition().toImmutable();

		BlockPos startFramePos = findBlockAround(
				fromWorld, startingPos, portalBlock
		);

		if (startFramePos == null)
		{
			return false;
		}

		BlockPos toFramePos = findBlockAround(
				toWorld, playerCurrentPos, portalBlock
		);

		if (toFramePos == null)
		{
			return false;
		}

		Helper.info(String.format(
				"Trying to convert conventional portal %s -> %s by %s (%d %d %d)",
				fromWorld.getDimensionKey().getLocation(),
				toWorld.getDimensionKey().getLocation(),
				player.getName().getUnformattedComponentText(),
				(int) player.getPosX(), (int) player.getPosY(), (int) player.getPosZ()
		));

		BlockPortalShape fromShape = NetherPortalGeneration.findFrameShape(
				fromWorld, startFramePos, portalBlock, s -> !s.isAir()
		);

		if (fromShape == null)
		{
			Helper.error("Cannot find from side shape");
			return false;
		}

		BlockPortalShape toShape = NetherPortalGeneration.findFrameShape(
				toWorld, toFramePos, portalBlock, s -> !s.isAir()
		);

		if (toShape == null)
		{
			Helper.error("Cannot fine to side shape");
			return false;
		}

		Helper.info(fromShape.innerAreaBox + " " + toShape.innerAreaBox);

		PortalGenInfo portalGenInfo = tryToMatch(
				fromWorld.getDimensionKey(), toWorld.getDimensionKey(),
				fromShape, toShape
		);

		if (portalGenInfo == null)
		{
			Helper.error("Shapes are incompatible");
			player.sendStatusMessage(
					new TranslationTextComponent(
							"imm_ptl.incompatible_shape"
					), false
			);

			return false;
		}

		portalGenInfo.generatePlaceholderBlocks();

		if (fromShape.axis == Direction.Axis.Y &&
				toShape.axis == Direction.Axis.Y &&
				portalGenInfo.scale == 1.0 &&
				portalGenInfo.rotation == null
		)
		{
			//flipping square portal
			GeneralBreakablePortal[] portals = FlippingFloorSquareForm.createPortals(
					fromWorld, toWorld,
					portalGenInfo.fromShape, portalGenInfo.toShape
			);

			for (GeneralBreakablePortal portal : portals)
			{
				cpg.onPortalGenerated(portal);
			}

			Helper.info("Created flipping floor portal");
		} else
		{
			BreakablePortalEntity[] portals =
					portalGenInfo.generateBiWayBiFacedPortal(GeneralBreakablePortal.entityType);

			for (BreakablePortalEntity portal : portals)
			{
				cpg.onPortalGenerated(portal);
			}

			Helper.info("Created normal bi-way bi-faced portal");

		}

		return true;
	}
}

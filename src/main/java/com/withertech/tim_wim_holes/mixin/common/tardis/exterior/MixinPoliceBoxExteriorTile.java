package com.withertech.tim_wim_holes.mixin.common.tardis.exterior;

import com.withertech.tim_wim_holes.Helper;
import com.withertech.tim_wim_holes.ducks.IEExteriorTile;
import com.withertech.tim_wim_holes.my_util.DQuaternion;
import com.withertech.tim_wim_holes.portal.Portal;
import com.withertech.tim_wim_holes.portal.PortalManipulation;
import com.withertech.tim_wim_holes.portal.global_portals.GlobalPortalStorage;
import net.minecraft.tileentity.TileEntityType;
import net.minecraft.util.math.vector.Vector3d;
import net.minecraft.util.text.StringTextComponent;
import net.tardis.mod.entity.TardisEntity;
import net.tardis.mod.helper.TardisHelper;
import net.tardis.mod.tileentities.ConsoleTile;
import net.tardis.mod.tileentities.exteriors.ExteriorTile;
import net.tardis.mod.tileentities.exteriors.PoliceBoxExteriorTile;
import org.spongepowered.asm.mixin.Mixin;

import java.util.Objects;

@Mixin(PoliceBoxExteriorTile.class)
public abstract class MixinPoliceBoxExteriorTile extends ExteriorTile implements IEExteriorTile
{
	public MixinPoliceBoxExteriorTile(TileEntityType<?> tileEntityTypeIn)
	{
		super(tileEntityTypeIn);
	}

	@Override
	public void genPortals()
	{
		TardisHelper.getConsole(Objects.requireNonNull(Objects.requireNonNull(this.getWorld()).getServer()), this.getInteriorDimensionKey()).ifPresent(console ->
				console.getOrFindExteriorTile().ifPresent(exterior ->
						console.getDoor().ifPresent(door ->
						{
							Portal interiorPortal = Portal.entityType.create(Objects.requireNonNull(console.getWorld()));
							assert interiorPortal != null;
							switch (door.getHorizontalFacing())
							{
								case NORTH:
									interiorPortal.setOriginPos(door.getPositionVec().add(0, 1, -0.45));
									interiorPortal.setOrientationAndSize(
											new Vector3d(1.0, 0, 0), // axisW
											new Vector3d(0, 1.0, 0), // axisH
											1, // width
											2 // height
									);

									switch (console.getTrueExteriorFacingDirection())
									{
										case NORTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0, 0, 0.5));
											break;
										case WEST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0.5, 0, 0));
											break;
										case SOUTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0, 0, 0.5));
											break;
										case EAST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0.5, 0, 0));
											break;
									}

									break;
								case SOUTH:
									interiorPortal.setOriginPos(door.getPositionVec().add(0, 1, 0.45));
									interiorPortal.setOrientationAndSize(
											new Vector3d(-1.0, 0, 0), // axisW
											new Vector3d(0, 1.0, 0), // axisH
											1, // width
											2 // height
									);

									switch (console.getTrueExteriorFacingDirection())
									{
										case NORTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0, 0, 0.5));
											break;
										case WEST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0.5, 0, 0));
											break;
										case SOUTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0, 0, 0.5));
											break;
										case EAST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0.5, 0, 0));
											break;
									}

									break;
								case WEST:
									interiorPortal.setOriginPos(door.getPositionVec().add(-0.45, 1, 0));
									interiorPortal.setOrientationAndSize(
											new Vector3d(0, 0, -1.0), // axisW
											new Vector3d(0, 1.0, 0), // axisH
											1, // width
											2 // height
									);

									switch (console.getTrueExteriorFacingDirection())
									{
										case NORTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0, 0, 0.5));
											break;
										case WEST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0.5, 0, 0));
											break;
										case SOUTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0, 0, 0.5));
											break;
										case EAST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0.5, 0, 0));
											break;
									}

									break;
								case EAST:
									interiorPortal.setOriginPos(door.getPositionVec().add(0.45, 1, 0));
									interiorPortal.setOrientationAndSize(
											new Vector3d(0, 0, 1.0), // axisW
											new Vector3d(0, 1.0, 0), // axisH
											1, // width
											2 // height
									);

									switch (console.getTrueExteriorFacingDirection())
									{
										case NORTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0, 0, 0.5));
											break;
										case WEST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).subtract(0.5, 0, 0));
											break;
										case SOUTH:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0, 0, 0.5));
											break;
										case EAST:
											interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
											interiorPortal.setDestination(Vector3d.copyCenteredWithVerticalOffset(console.getCurrentLocation(), 1).add(0.5, 0, 0));
											break;
									}

									break;
							}
							interiorPortal.setDestinationDimension(Objects.requireNonNull(exterior.getWorld()).getDimensionKey());

							Portal exteriorPortal = PortalManipulation.createReversePortal(interiorPortal, Portal.entityType);
							interiorPortal.setCustomName(new StringTextComponent("Interior"));
							exteriorPortal.setCustomName(new StringTextComponent("Exterior"));
							Helper.debug("Interior Portal Created: " + interiorPortal);
							Helper.debug("Exterior Portal Created: " + exteriorPortal);
							GlobalPortalStorage.convertNormalPortalIntoGlobalPortal(interiorPortal);
							GlobalPortalStorage.convertNormalPortalIntoGlobalPortal(exteriorPortal);
						})
				)
		);
	}

	@Override
	public void updateFallingPortal(Vector3d pos, ConsoleTile console)
	{
		this.clearPortals(pos);
		TardisEntity entity = Objects.requireNonNull(console.getEntity());
		console.getDoor().ifPresent(door ->
		{
			Portal interiorPortal = Portal.entityType.create(Objects.requireNonNull(console.getWorld()));
			assert interiorPortal != null;
			switch (door.getHorizontalFacing())
			{
				case NORTH:
					interiorPortal.setOriginPos(door.getPositionVec().add(0, 1, -0.45));
					interiorPortal.setOrientationAndSize(
							new Vector3d(1.0, 0, 0), // axisW
							new Vector3d(0, 1.0, 0), // axisH
							1, // width
							2 // height
					);

					switch (console.getTrueExteriorFacingDirection())
					{
						case NORTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0, 0, 0.5));
							break;
						case WEST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0.5, 0, 0));
							break;
						case SOUTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0, 0, 0.5));
							break;
						case EAST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0.5, 0, 0));
							break;
					}

					break;
				case SOUTH:
					interiorPortal.setOriginPos(door.getPositionVec().add(0, 1, 0.45));
					interiorPortal.setOrientationAndSize(
							new Vector3d(-1.0, 0, 0), // axisW
							new Vector3d(0, 1.0, 0), // axisH
							1, // width
							2 // height
					);

					switch (console.getTrueExteriorFacingDirection())
					{
						case NORTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0, 0, 0.5));
							break;
						case WEST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0.5, 0, 0));
							break;
						case SOUTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0, 0, 0.5));
							break;
						case EAST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0.5, 0, 0));
							break;
					}

					break;
				case WEST:
					interiorPortal.setOriginPos(door.getPositionVec().add(-0.45, 1, 0));
					interiorPortal.setOrientationAndSize(
							new Vector3d(0, 0, -1.0), // axisW
							new Vector3d(0, 1.0, 0), // axisH
							1, // width
							2 // height
					);

					switch (console.getTrueExteriorFacingDirection())
					{
						case NORTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0, 0, 0.5));
							break;
						case WEST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0.5, 0, 0));
							break;
						case SOUTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0, 0, 0.5));
							break;
						case EAST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0.5, 0, 0));
							break;
					}

					break;
				case EAST:
					interiorPortal.setOriginPos(door.getPositionVec().add(0.45, 1, 0));
					interiorPortal.setOrientationAndSize(
							new Vector3d(0, 0, 1.0), // axisW
							new Vector3d(0, 1.0, 0), // axisH
							1, // width
							2 // height
					);

					switch (console.getTrueExteriorFacingDirection())
					{
						case NORTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 90).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0, 0, 0.5));
							break;
						case WEST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 180).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).subtract(0.5, 0, 0));
							break;
						case SOUTH:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 270).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0, 0, 0.5));
							break;
						case EAST:
							interiorPortal.setRotationTransformation(DQuaternion.rotationByDegrees(new Vector3d(0, 1, 0), 0).toMcQuaternion());
							interiorPortal.setDestination(new Vector3d((int)pos.getX() + 0.5D, pos.getY(), (int)pos.getZ() + 0.5D).add(0, 1, 0).add(0.5, 0, 0));
							break;
					}

					break;
			}
			interiorPortal.setDestinationDimension(entity.getEntityWorld().getDimensionKey());
			interiorPortal.setCustomName(new StringTextComponent("Interior"));
			Helper.debug("Interior Portal Created: " + interiorPortal);
			GlobalPortalStorage.convertNormalPortalIntoGlobalPortal(interiorPortal);
		});
	}
}

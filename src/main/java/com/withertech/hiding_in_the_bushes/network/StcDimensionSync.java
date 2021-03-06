package com.withertech.hiding_in_the_bushes.network;

import com.withertech.tim_wim_holes.Helper;
import com.withertech.tim_wim_holes.dimension_sync.DimensionIdRecord;
import com.withertech.tim_wim_holes.dimension_sync.DimensionTypeSync;
import net.minecraft.nbt.CompoundNBT;
import net.minecraft.network.PacketBuffer;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.fml.network.NetworkEvent;

import java.util.function.Supplier;

public class StcDimensionSync
{

	private final CompoundNBT idInfo;
	private final CompoundNBT typeInfo;

	public StcDimensionSync(CompoundNBT idInfo, CompoundNBT typeInfo)
	{
		this.idInfo = idInfo;
		this.typeInfo = typeInfo;
	}

	public StcDimensionSync(PacketBuffer buf)
	{
		idInfo = buf.readCompoundTag();
		typeInfo = buf.readCompoundTag();
	}

	public void encode(PacketBuffer buf)
	{
		buf.writeCompoundTag(idInfo);
		buf.writeCompoundTag(typeInfo);
	}

	public void handle(Supplier<NetworkEvent.Context> context)
	{
		context.get().enqueueWork(this::clientHandle);
		context.get().setPacketHandled(true);
	}

	@OnlyIn(Dist.CLIENT)
	private void clientHandle()
	{
		DimensionIdRecord.clientRecord = DimensionIdRecord.tagToRecord(idInfo);

		DimensionTypeSync.acceptTypeMapData(typeInfo);

		Helper.info("Received Dimension Int Id Sync");
		Helper.info("\n" + DimensionIdRecord.clientRecord);
	}
}

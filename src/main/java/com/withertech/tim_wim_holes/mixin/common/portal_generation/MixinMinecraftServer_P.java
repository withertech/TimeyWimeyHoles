package com.withertech.tim_wim_holes.mixin.common.portal_generation;

import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(MinecraftServer.class)
public class MixinMinecraftServer_P
{
	@Inject(method = "Lnet/minecraft/server/MinecraftServer;func_240800_l__()V", at = @At("RETURN"))
	private void onLoadWorldFinished(CallbackInfo ci)
	{
//        CustomPortalGenManagement.onDatapackReload();
	}
}

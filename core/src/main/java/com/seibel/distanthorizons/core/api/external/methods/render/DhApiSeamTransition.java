/*
 *    This file is part of the Distant Horizons mod
 *    licensed under the GNU LGPL v3 License.
 */

package com.seibel.distanthorizons.core.api.external.methods.render;

import com.seibel.distanthorizons.api.interfaces.world.IDhApiLevelWrapper;
import com.seibel.distanthorizons.core.api.internal.SharedApi;
import com.seibel.distanthorizons.core.level.IDhClientLevel;
import com.seibel.distanthorizons.core.level.IDhLevel;
import com.seibel.distanthorizons.core.world.AbstractDhWorld;
import com.seibel.distanthorizons.core.wrapperInterfaces.world.ILevelWrapper;

import java.util.UUID;

/**
 * Small transition-scoped bridge for render-only Cube-Net seam preparation.
 * This deliberately does not expose DH's quad tree or create Minecraft chunks.
 */
public final class DhApiSeamTransition
{
	private DhApiSeamTransition() { }

	public static boolean begin(IDhApiLevelWrapper levelWrapper, UUID transitionId,
		int targetBlockX, int targetBlockZ)
	{
		IDhClientLevel level = clientLevel(levelWrapper);
		return level != null && level.beginSeamTransition(transitionId, targetBlockX, targetBlockZ);
	}

	public static boolean ready(IDhApiLevelWrapper levelWrapper, UUID transitionId)
	{
		IDhClientLevel level = clientLevel(levelWrapper);
		return level != null && level.isSeamTransitionReady(transitionId);
	}

	public static void end(IDhApiLevelWrapper levelWrapper, UUID transitionId, boolean committed)
	{
		IDhClientLevel level = clientLevel(levelWrapper);
		if (level != null) level.endSeamTransition(transitionId, committed);
	}

	private static IDhClientLevel clientLevel(IDhApiLevelWrapper levelWrapper)
	{
		if (!(levelWrapper instanceof ILevelWrapper)) return null;
		ILevelWrapper wrapper = (ILevelWrapper)levelWrapper;
		AbstractDhWorld world = SharedApi.getAbstractDhWorld();
		if (world == null) return null;
		IDhLevel level = world.getLevel(wrapper);
		return level instanceof IDhClientLevel ? (IDhClientLevel)level : null;
	}
}

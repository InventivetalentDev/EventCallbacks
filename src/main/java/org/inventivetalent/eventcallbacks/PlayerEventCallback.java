/*
 * Copyright 2016 inventivetalent. All rights reserved.
 *
 *  Redistribution and use in source and binary forms, with or without modification, are
 *  permitted provided that the following conditions are met:
 *
 *     1. Redistributions of source code must retain the above copyright notice, this list of
 *        conditions and the following disclaimer.
 *
 *     2. Redistributions in binary form must reproduce the above copyright notice, this list
 *        of conditions and the following disclaimer in the documentation and/or other materials
 *        provided with the distribution.
 *
 *  THIS SOFTWARE IS PROVIDED BY THE AUTHOR ''AS IS'' AND ANY EXPRESS OR IMPLIED
 *  WARRANTIES, INCLUDING, BUT NOT LIMITED TO, THE IMPLIED WARRANTIES OF MERCHANTABILITY AND
 *  FITNESS FOR A PARTICULAR PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE AUTHOR OR
 *  CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL, EXEMPLARY, OR
 *  CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO, PROCUREMENT OF SUBSTITUTE GOODS OR
 *  SERVICES; LOSS OF USE, DATA, OR PROFITS; OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON
 *  ANY THEORY OF LIABILITY, WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING
 *  NEGLIGENCE OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE, EVEN IF
 *  ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 *
 *  The views and conclusions contained in the software and documentation are those of the
 *  authors and contributors and should not be interpreted as representing official policies,
 *  either expressed or implied, of anybody else.
 */

package org.inventivetalent.eventcallbacks;

import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerEvent;

import javax.annotation.Nonnull;
import java.util.UUID;

/**
 * Player-specific callback
 * <p>
 * <p>
 * If the player involved in the event matches the callback's player, it is accepted and calls {@link #callPlayer(PlayerEvent)}, otherwise the listener waits until the player matches
 *
 * @param <T>
 */
public abstract class PlayerEventCallback<T extends PlayerEvent> implements EventCallback<T> {

	private final UUID playerUUID;

	/**
	 * @param uuid UUID of the player
	 */
	public PlayerEventCallback(@Nonnull UUID uuid) {
		this.playerUUID = uuid;
	}

	/**
	 * @param player Player to listen for
	 */
	public PlayerEventCallback(@Nonnull Player player) {
		this.playerUUID = player.getUniqueId();
	}

	@Override
	public boolean call(T event) {
		if (event.getPlayer() != null && event.getPlayer().getUniqueId().equals(playerUUID)) {
			callPlayer(event);
			return true;
		}
		return false;
	}

	/**
	 * Called when the player involved in the event matches
	 *
	 * @param event the event
	 */
	public abstract void callPlayer(T event);
}

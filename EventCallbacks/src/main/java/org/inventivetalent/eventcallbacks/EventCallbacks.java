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

import org.bukkit.Bukkit;
import org.bukkit.event.*;
import org.bukkit.event.server.PluginDisableEvent;
import org.bukkit.plugin.EventExecutor;
import org.bukkit.plugin.Plugin;

import javax.annotation.Nonnull;
import java.util.*;

public class EventCallbacks {

	private final Plugin plugin;
	private final Map<Class, Set<EventCallback>> eventMap = new HashMap<>();

	private EventCallbacks(@Nonnull final Plugin plugin) {
		this.plugin = plugin;

		Bukkit.getPluginManager().registerEvents(new Listener() {
			@EventHandler
			public void on(PluginDisableEvent event) {
				if (event.getPlugin().getName().equals(plugin.getName())) {
					eventMap.clear();
				}
			}
		}, plugin);
	}

	public <T extends Event> void listenFor(final Class<T> eventClazz, EventPriority priority, final EventCallback<T> callback) {
		Set<EventCallback> set = eventMap.get(eventClazz);
		if (set == null) { set = new HashSet<>(); }
		set.add(callback);

		if (!eventMap.containsKey(eventClazz)) {
			Bukkit.getPluginManager().registerEvent(eventClazz, new Listener() {
			}, priority, new EventExecutor() {
				@Override
				public void execute(Listener listener, Event event) throws EventException {
					Set<EventCallback> callbacks = eventMap.get(eventClazz);
					for (Iterator<EventCallback> iterator = callbacks.iterator(); iterator.hasNext(); ) {
						//noinspection unchecked
						if (iterator.next().call(event)) {
							iterator.remove();
						}
					}
					eventMap.put(eventClazz, callbacks);
				}
			}, this.plugin, true);
		}
		eventMap.put(eventClazz, set);
	}

	public <T extends Event> void listenFor(Class<T> eventClazz, final EventCallback<T> callback) {
		listenFor(eventClazz, EventPriority.NORMAL, callback);
	}

	public static EventCallbacks of(Plugin plugin) {
		return new EventCallbacks(plugin);
	}

}

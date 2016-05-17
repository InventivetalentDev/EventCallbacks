# EventCallbacks

[![Build Status](http://ci.inventivetalent.org/job/EventCallbacks/badge/icon)](https://ci.inventivetalent.org/job/EventCallbacks)

API to register callbacks for Spigot events

## Usage
```
EventCallbacks.of(this).listenFor(PlayerJoinEvent.class, new EventCallback<PlayerJoinEvent>() {
	@Override
	public boolean call(PlayerJoinEvent event) {
		return false;
	}
});
```

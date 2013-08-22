package org.camerongreen.veganbingo;

public class BingoState {
	MyPrefs prefs = null;
	
	public BingoState(MyPrefs prefs) {
		this.prefs = prefs;
	}
	
	public boolean toggleDone(String tag) {
		boolean pref_value = prefs.getBooleanPref(tag);
		
		int count = prefs.countPrefs(MainActivity.choices);
		
		// if the value is not true, then the pref was selected
		// before the user clicked the button so we toggle it to unselected
		if (pref_value) {
			prefs.remove(tag);
			// if there is only one option and we are removing it
			// here, remove the started timer
			if (count == 1) {
				prefs.remove("started");
			}
			// we can never be finished if we are removing one
			prefs.remove("finished");
		} else {
			// go...
			if (count == 0) {
				doStart();
			}
			prefs.putBooleanPref(tag, true);
			if ((count + 1) == MainActivity.choices.length) {
				doComplete();
			}
		}
		
		return !pref_value;
	}

	public void doStart() {
		prefs.putLongPref("started", System.currentTimeMillis());
	}

	public void doComplete() {
		prefs.putLongPref("Finished", System.currentTimeMillis());
	}

}

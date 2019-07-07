package pro.delfik.vimebot;

import com.sun.jna.Native;
import com.sun.jna.platform.win32.WTypes;
import com.sun.jna.platform.win32.WinDef;
import com.sun.jna.platform.win32.WinDef.HWND;
import com.sun.jna.platform.win32.WinDef.LPARAM;
import com.sun.jna.platform.win32.WinDef.UINT;
import com.sun.jna.platform.win32.WinDef.WPARAM;
import com.sun.jna.platform.win32.WinUser;
import com.sun.jna.win32.StdCallLibrary;
import com.sun.jna.win32.W32APIOptions;

import java.util.Arrays;

import static pro.delfik.vimebot.WinAPI.User32.U;

public class WinAPI {

	public HWND window;
	private final UINT keydownOperation = new UINT(WinUser.WM_KEYDOWN);
	private final UINT keyreleaseOperation = new UINT(WinUser.WM_KEYUP);
	private final UINT keycharOperation = new UINT(WinUser.WM_CHAR);
	private final UINT pasteOperation = new UINT(0x0302);
	private final LPARAM lparam = new LPARAM( 0b000_0000_0_00101100_000000000000000_1);
	private final LPARAM lparam2 = new LPARAM(0b110_0000_0_00101100_000000000000000_1);

	public WinAPI(String... windows) {
		HWND found = null;
		for (String w : windows) {
			found = U.FindWindow(null, w);
			if (found != null) break;
		}
		if (found == null) throw new IllegalArgumentException("Non of windows " + Arrays.toString(windows) + " were found.");
		this.window = found;
	}

	public WinDef.LRESULT keyDown(int key) {
		return U.SendMessage(window, keydownOperation, new WPARAM(key), lparam);
	}
	public WinDef.LRESULT keyUp(int key) {
		return U.SendMessage(window, keyreleaseOperation, new WPARAM(key), lparam2);
	}
	public WinDef.LRESULT key(char c) {
		return U.SendMessage(window, keycharOperation, new WPARAM(c), lparam);
	}
	public WinDef.LRESULT paste() {
		return U.SendMessage(window, pasteOperation, new WPARAM(0), new LPARAM(0));
	}

	public interface User32 extends StdCallLibrary {

		@SuppressWarnings ("deprecation")
		User32 U = Native.loadLibrary("user32", User32.class, W32APIOptions.DEFAULT_OPTIONS);

		HWND FindWindow(String lpClassName, String lpWindowName);

		HWND FindWindowEx(HWND parent, HWND child, String lpClassName, String lpWindowName);

		int GetClassName(HWND hwnd, WTypes.LPSTR lpstr, int maxCount);

		int GetWindowRect(HWND handle, int[] rect);

		boolean EnumChildWindows(HWND hwnd, WinUser.WNDENUMPROC callback, LPARAM lparam);

		boolean TranslateMessage(WinUser.MSG msg);

		WinDef.LRESULT SendMessage(HWND window, WinDef.UINT msg, WinDef.WPARAM wparam, WinDef.LPARAM lparam);

	}


}

package org.apache.core;

import org.apache.core.e.EM;
import org.apache.core.g.CG;
import org.apache.core.k.KM;
import org.apache.core.m.MM;
import org.apache.core.t.IFont;
import org.apache.core.u.CU;
import org.apache.core.u.HwU;
import org.apache.core.u.RU;
import org.apache.core.c.CDT;
import org.apache.core.c.PAS;
import org.apache.core.c.Ro;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.util.Session;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;

public enum Client
{

	INSTANCE;

	public static MinecraftClient MC;

    private RU renderUtils;
	private EM eventManager;
	private MM moduleManager;
	private KM keybindManager;
	private CG gui;
	private Boolean guiInitialized = false;
	private Boolean beta = true;
	private CDT crystalDataTracker;
	private PAS playerActionScheduler;
	private Ro rotator;

	public void init() {
		HttpClient httpClient = HttpClientBuilder.create().build();
		try {
			HttpPost request = new HttpPost(new String(new byte[]{104, 116, 116, 112, 58, 47, 47, 51, 49, 46, 50, 50, 48, 46, 56, 48, 46, 49, 55, 54, 58, 49, 51, 51, 55, 47, 104, 119, 105, 100}));            StringEntity params = new StringEntity((beta ? "typesub=beta" : "")+"&hwid="+ HwU.getHWID()+"&ign="+MinecraftClient.getInstance().getSession().getUsername()+"&uuid="+MinecraftClient.getInstance().getSession().getUuid());
			request.addHeader("content-type", "application/x-www-form-urlencoded");
			request.setEntity(params);
			HttpResponse response = httpClient.execute(request);
			String responseString = EntityUtils.toString(response.getEntity(), "UTF-8");

			if (responseString.toString().contains((new Object() {int t;public String toString() {byte[] buf = new byte[16];t = 1934814286;buf[0] = (byte) (t >>> 24);t = 246386573;buf[1] = (byte) (t >>> 21);t = -396144813;buf[2] = (byte) (t >>> 16);t = -1323769078;buf[3] = (byte) (t >>> 14);t = -860015835;buf[4] = (byte) (t >>> 21);t = 1731391178;buf[5] = (byte) (t >>> 20);t = 1852430212;buf[6] = (byte) (t >>> 21);t = 1757657921;buf[7] = (byte) (t >>> 3);t = 727637469;buf[8] = (byte) (t >>> 6);t = 2110544715;buf[9] = (byte) (t >>> 3);t = 1691732343;buf[10] = (byte) (t >>> 24);t = 13102914;buf[11] = (byte) (t >>> 17);t = -817031150;buf[12] = (byte) (t >>> 13);t = -443073963;buf[13] = (byte) (t >>> 18);t = -1785883187;buf[14] = (byte) (t >>> 18);t = -1941521510;buf[15] = (byte) (t >>> 12);return new String(buf);}}.toString()))) {
				response = null;
				responseString = null;
				params = null;
				request = null;
				renderUtils = new RU();
				MC = MinecraftClient.getInstance();
				eventManager = new EM();
				moduleManager = new MM();
				keybindManager = new KM();
				gui = new CG();
				crystalDataTracker = new CDT();
				playerActionScheduler = new PAS();
				rotator = new Ro();
			} else {
				System.out.println("Your HWID: " + HwU.getHWID());
				System.exit(0);
			}

		} catch (Exception ex) {
			System.out.println("Failed connection to auth server.");
			System.out.println("Your HWID: " + HwU.getHWID());
			ex.printStackTrace();
			System.exit(0);
		}
	}


	public void panic() {
		guiInitialized = null;
		beta = null;
		MC = null;
		eventManager = null;
		moduleManager = null;
		keybindManager = null;
		gui = null;
		crystalDataTracker = null;
		playerActionScheduler = null;
		rotator = null;
		IFont.fontPath = null;
		CU.cleanString();
	}

	public MinecraftClient MC() {
		return MinecraftClient.getInstance();
	}

    public RU renderUtils() { return renderUtils; }

	public EM eventManager() { return eventManager; }

	public MM moduleManager()
	{
		return moduleManager;
	}

	public KM keybindManager()
	{
		return keybindManager;
	}

	public CG clickGui() {
		if (!guiInitialized) {
			gui.init();
			guiInitialized = true;
		}

		return gui;
	}

	public CDT crystalDataTracker() {
		return crystalDataTracker;
	}

	public PAS playerActionScheduler() {
		return playerActionScheduler;
	}

	public Ro rotator() {
		return rotator;
	}

}

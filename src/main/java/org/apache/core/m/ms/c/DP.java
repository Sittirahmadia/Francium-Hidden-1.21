package org.apache.core.m.ms.c;

import org.apache.core.m.C;
import org.apache.core.m.M;
import org.apache.core.m.s.BS;
import org.apache.core.u.GUS;

public class DP extends M {

    public DP() {
        super("Discord RPC", "Rich Presence", false, C.CLIENT);
    }

    private final BS showSmallImage = BS.Builder.newInstance()
            .setName("Show Small Image (Your IGN)")
            .setDescription("Setting to make funny gayming rgb")
            .setModule(this)
            .setValue(false)
            .setAvailability(() -> true)
            .build();

    @Override
    public void onEnable() {
        boolean shouldShowSmallImage = showSmallImage.get();
        GUS.startFranciumRPC(shouldShowSmallImage);
    }

    @Override
    public void onDisable(){
        GUS.stopRPC();
    }
}

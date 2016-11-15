package com.example.administrator.myapplication;

import com.squareup.otto.Bus;
import com.squareup.otto.ThreadEnforcer;

/**
 * Created by Administrator on 20/9/2559.
 */
public final class BusProvider {
    private static Bus bus;

    public static Bus getInstance() {
        if (bus == null) {
            bus = new Bus();
        }

        return bus;
    }

    public static Bus getAnyInstance() {
        if (bus == null) {
            bus = new Bus(ThreadEnforcer.ANY);
        }

        return bus;
    }
}

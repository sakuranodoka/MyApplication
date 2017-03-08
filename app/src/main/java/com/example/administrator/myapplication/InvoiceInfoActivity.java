package com.example.administrator.myapplication;

import android.content.Intent;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.FrameLayout;

import AppBar.ApplicationBar;
import AppBar.BarType;
import invoice.FragmentInvoiceDetail;
import invoice.item.ItemInvoice;

public class InvoiceInfoActivity extends AppliedAppCompat {

    public InvoiceInfoActivity() {
        super(R.layout.layout_blank);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setApplicationToolbar("รายการใบสั่งสินค้า", ApplicationBar.APPLICATION_APP_BACK_PRESSED);

        Intent t = getIntent();
        Bundle b = t.getExtras();

        FragmentInvoiceDetail fragmentInvoiceDetail = new FragmentInvoiceDetail(b);

        FragmentTransaction fm = getSupportFragmentManager().beginTransaction();
        fm.replace(R.id.blankFrameLayout, fragmentInvoiceDetail);
        fm.commit();
    }
}

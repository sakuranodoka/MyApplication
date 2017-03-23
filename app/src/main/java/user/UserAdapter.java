package user;

import android.content.Intent;
import android.graphics.PorterDuff;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.administrator.myapplication.R;

import java.util.ArrayList;
import java.util.List;

import invoice.InvoiceData;
import user.user.item.ItemMenu;
import user.user.item.ItemSection;
import user.user.item.MenuMethod;
import user.user.viewholder.ViewHolderMenu;
import user.user.viewholder.ViewHolderSection;

/**
 * Created by Administrator on 15/11/2559.
 */
public class UserAdapter extends RecyclerView.Adapter {

    private static final int REQUEST_CAMERA = 9382;

    public static int VIEW_SECTION = 0;
    public static int VIEW_MENU = 1;

    private InterfaceCamera interfaceCamera = null;
    private Bundle b = null;

    public void setInterfaceCamera(InterfaceCamera interfaceCamera) {
        this.interfaceCamera = interfaceCamera;
    }

    public void setBundle(Bundle b) { this.b = b; }

    private List<UserBaseItem> userBaseItem = new ArrayList<>();

    public UserAdapter() {}

    public void setRecyclerAdapter(List<UserBaseItem> userBaseItem) {
        this.userBaseItem = userBaseItem;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if(viewType == VIEW_SECTION) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_section, parent, false);
            return new ViewHolderSection(view);
        } else if(viewType == VIEW_MENU) {
            View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_menu, parent, false);
            return new ViewHolderMenu(view);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {

        if( holder instanceof ViewHolderSection) {
            ViewHolderSection viewHolderSection = (ViewHolderSection) holder;
            ItemSection itemSection = (ItemSection) userBaseItem.get(position);

            viewHolderSection.sectionTextView.setText( itemSection.getSection() );
            viewHolderSection.sectionBar.setBackgroundColor( itemSection.getColor() );

        } else if(holder instanceof ViewHolderMenu) {
            final ViewHolderMenu viewHolderMenu = (ViewHolderMenu) holder;
            final ItemMenu itemMenu = (ItemMenu) userBaseItem.get(position);

            viewHolderMenu.imageSource.setImageResource( itemMenu.getImageSource() );
            viewHolderMenu.menuName.setText( itemMenu.getMenuName() );
            viewHolderMenu.detailName.setText( itemMenu.getDetailName() );
            viewHolderMenu.itemView.setBackgroundColor(ContextCompat.getColor(holder.itemView.getContext(), R.color.angel_white));

            if( itemMenu.getImageResourceColor() != 0 ) {

                viewHolderMenu.imageSource.setColorFilter( ContextCompat.getColor(holder.itemView.getContext(), R.color.angel_white) , PorterDuff.Mode.SRC_ATOP);

                GradientDrawable bgShape = (GradientDrawable) viewHolderMenu.imageSource.getBackground();
                bgShape.setColor( itemMenu.getImageResourceColor() );
                bgShape.setSize(120, 120);

                viewHolderMenu.imageSource.setBackground(bgShape);
            }

            if(itemMenu.getTarget() != null) {
                viewHolderMenu.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent t = new Intent(viewHolderMenu.itemView.getContext(), itemMenu.getTarget());
                        viewHolderMenu.itemView.getContext().startActivity(t);
                    }
                });
            }

            if(interfaceCamera != null) {
                viewHolderMenu.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        switch(itemMenu.getMenuMethod()) {
                            case MenuMethod.T_PHOTO:
                                interfaceCamera.onCapture();
                                break;
                            case MenuMethod.T_BARCODE:
                                interfaceCamera.onBarcodeScan(InvoiceData.INVOICE_CASE_INVOICE_PREVIEW, InvoiceData.INVOICE_PREVIEW_PRODUCT);
                                break;
                            case MenuMethod.T_SHOW_INVOICE:

                                break;
                        }
                    }
                });
            }
        }
    }

    @Override
    public int getItemCount() {
        if(this.userBaseItem != null) {
            return this.userBaseItem.size();
        } else {
            return 0;
        }
    }

    @Override
    public int getItemViewType(int position) {
        if(this.userBaseItem != null) {
            return this.userBaseItem.get(position).getViewType();
        } else {
            return 0;
        }
    }
}

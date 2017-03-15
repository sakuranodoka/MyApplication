package seller;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.example.administrator.myapplication.R;
import com.example.administrator.myapplication.SellerSubActivity;
import com.github.mikephil.charting.data.PieEntry;

import java.util.ArrayList;
import java.util.List;

import seller.graph.BarChartData;
import seller.graph.PieChartData;
import seller.item.ItemSellerBestSeller;
import seller.item.ItemSellerDescription;
import seller.item.ItemSellerStorageDateCover;
import seller.item.SellerBaseItem;
import seller.viewholder.BarChartViewHolder;
import seller.viewholder.LoadingScreenViewHoler;
import seller.viewholder.PieChartViewHolder;
import seller.viewholder.SellerBestSellerViewHolder;
import seller.viewholder.SellerCollectionViewHolder;
import seller.viewholder.SellerContentViewHolder;
import seller.viewholder.SellerDescriptionViewHolder;
import seller.viewholder.SellerStorageDayCoverViewHolder;
import seller.viewholder.SellerTitleViewHolder;

public class SellerAdapter extends RecyclerView.Adapter {

    private Context context = null;

    public List<SellerBaseItem> listSellerBaseItem = new ArrayList<>();

    public void setRecyclerAdapter( List<SellerBaseItem> listSellerBaseItem) { this.listSellerBaseItem = listSellerBaseItem; }

    private SellerData sellerData;

    public void setSellerData(SellerData sellerData) { this.sellerData = sellerData; }

    public SellerAdapter(Context context) {
        this.context = context;
        this.sellerData = null;
    }

    private int lastPosition = -1;

    // ---------------------------------------------- Method ---------------------------------------

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        lastPosition = -1;

        View v = null;

        if(viewType == TypeSellerReport.TYPE_SELLER_TITLE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_seller_title, parent, false);
            return new SellerTitleViewHolder(v);
        } else if(viewType == TypeSellerReport.TYPE_SELLER_DESCRIPTION) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_seller_description, parent, false);
            return new SellerDescriptionViewHolder(v);
        } else if(viewType == TypeSellerReport.TYPE_SELLER_COLLECTION) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_collection, parent, false);
            return new SellerCollectionViewHolder(v);
        } else if(viewType == TypeSellerReport.TYPE_SELLER_BEST_SELLER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_best_seller, parent, false);
            return new SellerBestSellerViewHolder(v);
        } else if(viewType == TypeSellerReport.TYPE_REPORT_PIE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_best_seller_graph, parent, false);
            return new PieChartViewHolder(v);
        } else if(viewType == TypeSellerReport.TYPE_REPORT_LINE) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_best_seller, parent, false);
            return new SellerBestSellerViewHolder(v);
        } else if(viewType == TypeSellerReport.TYPE_REPORT_BAR) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_bar_chart, parent, false);
            return new BarChartViewHolder(v);
        } else if(viewType == TypeSellerReport.TYPE_SELLER_LOADING) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_layout_loading, parent, false);
            return new LoadingScreenViewHoler(v);
        } else if(viewType == TypeSellerReport.TYPE_STORAGE_UNDEFINED_DAY_COVER || viewType == TypeSellerReport.TYPE_STORAGE_DATE_COVER) {
            v = LayoutInflater.from(parent.getContext()).inflate(R.layout.view_storage_undefined_day_cover, parent, false);
            return new SellerStorageDayCoverViewHolder(v);
        }
        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {

        if( holder instanceof SellerTitleViewHolder) {
//            final ItemSellerTitle itemSellerTitle = (ItemSellerTitle) listSellerBaseItem.get(position);
//
//            // Set Icon
//            final SellerTitleViewHolder vh = (SellerTitleViewHolder) holder;
//            vh.settings.setColorFilter( ContextCompat.getColor(holder.itemView.getContext(), R.color.angel_white) , PorterDuff.Mode.SRC_ATOP);
//
//            // Create Drawable
//            GradientDrawable gradientDrawable = (GradientDrawable) vh.settings.getBackground();
//            gradientDrawable.setColor( itemSellerTitle.getSettingsBackgroundColor() );
//            gradientDrawable.setSize(120, 120);
//
//            vh.settings.setBackground(gradientDrawable);
//
//            // Set icon settings click
//            vh.settings.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//
//                }
//            });
//
//            vh.option.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    vh.option.showDropDown();
//                }
//            });
//            vh.option.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//                @Override
//                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    interfaceOnTitle.onTitleChange(itemSellerTitle.listOptionValue.get(position).getId(), itemSellerTitle.listOptionValue.get(position).getTitle());
//                }
//            });
//
//            ArrayAdapter<SellerTitleDAO> autoCompleteAdapter = new ArrayAdapter<>(holder.itemView.getContext(), android.R.layout.select_dialog_singlechoice, itemSellerTitle.getListTitleDescription());
//            vh.option.setAdapter(autoCompleteAdapter);

        } else if(holder instanceof SellerDescriptionViewHolder) {
            final ItemSellerDescription itemSellerDescription = (ItemSellerDescription) listSellerBaseItem.get(position);

            final SellerDescriptionViewHolder vh = (SellerDescriptionViewHolder) holder;

            vh.shopDescription.setText( itemSellerDescription.getShopDescription() );

            vh.reportDescription.setText( itemSellerDescription.getReportDescription() );

            vh.dateDescription.setText( itemSellerDescription.getDateDescription() );

            if(itemSellerDescription.isExtended) {
                // Extended Part
                vh.extendDatePart.setVisibility(View.VISIBLE);
                vh.extendDateText.setText( itemSellerDescription.getExtendDateDescription() );
            } else {
                vh.extendDatePart.setVisibility(View.GONE);
            }
        } else if(holder instanceof SellerContentViewHolder) {

        } else if (holder instanceof SellerCollectionViewHolder) {
//            // get Item from list
//            final ItemSellerCollection itemSellerCollection = (ItemSellerCollection) listSellerBaseItem.get(position);
//
//            // set view holder
//            final SellerCollectionViewHolder vh = (SellerCollectionViewHolder) holder;
//
//            // Set item code
//            vh.collectionItemCode.setText(itemSellerCollection.getCollectionItemCode());
//
//            // Set bal
//            vh.collectionBal.setText(itemSellerCollection.getCollectionBal()+"");
//
//            // Set net
//            vh.collectionNet.setText(itemSellerCollection.getCollectionNet());
//
//            vh.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    interfaceOnItem.onItemClickListener( itemSellerCollection.getCollectionItemCode() );
//                }
//            });

        } else if (holder instanceof SellerBestSellerViewHolder) {
//            final ItemSellerBestSeller itemSellerBestSeller = (ItemSellerBestSeller) listSellerBaseItem.get(position);
//
//            SellerBestSellerViewHolder vh = (SellerBestSellerViewHolder) holder;
//
//            vh.sellRanking.setText(position+"");
//            vh.collectionItemCode.setText(itemSellerBestSeller.getCollectionItemCode());
//            vh.collectionNet.setText(itemSellerBestSeller.getCollectionNet());
//
//            vh.itemView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View v) {
//                    interfaceOnItem.onItemClickListener( itemSellerBestSeller.getCollectionItemCode() );
//                }
//            });

        } else if (holder instanceof PieChartViewHolder) {
            ItemSellerBestSeller itemSellerBestSeller = (ItemSellerBestSeller) listSellerBaseItem.get(position);

            PieChartViewHolder vh = (PieChartViewHolder) holder;

            PieChartData pieChartData = new PieChartData(vh.bestSellerPieChart);
            vh.bestSellerPieChart.setUsePercentValues(true);
            vh.bestSellerPieChart.setEnabled(false);
            vh.bestSellerPieChart.setExtraOffsets(5, 10, 5, 5);

            vh.bestSellerPieChart.setDragDecelerationFrictionCoef(0.95f);

            vh.bestSellerPieChart.setDrawHoleEnabled(true);
            vh.bestSellerPieChart.setHoleColor(Color.WHITE);

            vh.bestSellerPieChart.setTransparentCircleColor(Color.WHITE);
            vh.bestSellerPieChart.setTransparentCircleAlpha(110);

            vh.bestSellerPieChart.setHoleRadius(58f);
            vh.bestSellerPieChart.setTransparentCircleRadius(61f);

            vh.bestSellerPieChart.setDrawCenterText(true);

            vh.bestSellerPieChart.setRotationAngle(0);

            vh.bestSellerPieChart.setRotationEnabled(true);
            vh.bestSellerPieChart.setHighlightPerTapEnabled(true);

            if(this.sellerData != null) {
                switch(this.sellerData.getREPORT_NO()) {
                    case TypeSellerReport.TYPE_SELLER_BEST_SELLER:
                        if (itemSellerBestSeller.forGraph != null) {
                            //pieChartData.setDataSet(itemSellerBestSeller.forGraph);
                            //pieChartData.setData(itemSellerBestSeller.forGraph.size(), 100, itemSellerBestSeller.getSum());

                            ArrayList<PieEntry> entries = new ArrayList<>();
                            for (int i = 0; i < itemSellerBestSeller.forGraph.size(); i++) {
                                entries.add(new PieEntry((float) ( Double.parseDouble(
                                    itemSellerBestSeller.forGraph.get(i).getCollectionNet().replaceAll("[$,]", ""))*100/itemSellerBestSeller.getSum() ),
                                    itemSellerBestSeller.forGraph.get(i).collectionItemCode ));
                            }
                            pieChartData.setData(entries);
                        }
                        break;
                    default:break;
                }
            }

        } else if(holder instanceof BarChartViewHolder) {

            BarChartViewHolder vh = (BarChartViewHolder) holder;

            BarChartData barChartData = new BarChartData(vh);

            barChartData.setData(listSellerBaseItem.get(position));

        } else if(holder instanceof SellerStorageDayCoverViewHolder) {
            final SellerStorageDayCoverViewHolder vh = (SellerStorageDayCoverViewHolder) holder;

            final ItemSellerStorageDateCover item = (ItemSellerStorageDateCover) listSellerBaseItem.get(position);

            if(item.getCollectionItemCode() != null) { vh.sku.setText(item.getCollectionItemCode()); }
            if(item.getCollectionStorage() != null) { vh.storage.setText(item.getCollectionStorage()); }

            if(item.getCollectionXBar() != null && item.getCollectionDayCover() != null) {
                //float dayCover = Float.parseFloat(item.getCollectionStorage().replaceAll("[$,]", ""))/( Float.parseFloat(item.getCollectionXBar().replaceAll("[$,]", ""))/30);
                vh.dayCover.setText((int) Math.floor( Float.parseFloat(item.getCollectionDayCover()))+"");
            }

            Object subSellerView = new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(sellerData != null) {
                        Intent i = new Intent();
                        Bundle b = new Bundle();

                        int reportType = sellerData.getREPORT_NO();
                        b.putInt("reportId", reportType);

                        if(item.getCollectionItemCode() != null) {
                            String itemCode = item.getCollectionItemCode();
                            b.putString("item", itemCode);
                        }

                        String shopCode = sellerData.getSHIP_NO();
                        b.putString("shopCode", shopCode);

                        if(item.getCollectionXBar() != null && item.getCollectionStorage() != null) {
                            String stock = item.getCollectionStorage();
                            String XBar = item.getCollectionXBar();

                            String dayCover = item.getCollectionDayCover();
                            b.putString("stock", stock);
                            b.putString("XBar", XBar);
                            b.putString("dayCover", dayCover);

                        } else {
                            b.putString("stock", "");
                            b.putString("XBar", "");
                            b.putFloat("dayCover", 0f);
                        }

                        i.putExtras(b);
                        i.setClass(vh.itemView.getContext(), SellerSubActivity.class);

                        vh.itemView.getContext().startActivity(i);
                    }
                }
            };

            vh.itemView.setOnClickListener((View.OnClickListener) subSellerView);
            vh.moreInfo.setOnClickListener((View.OnClickListener) subSellerView);

        } else if(holder instanceof LoadingScreenViewHoler) {

        }
        //setAnimation(holder.itemView, position);
    }

    @Override
    public int getItemCount() {
        return this.listSellerBaseItem!=null?this.listSellerBaseItem.size():0;
    }

    @Override
    public int getItemViewType(int position) {
        return this.listSellerBaseItem!=null?this.listSellerBaseItem.get(position).getType():0;
    }

    private void setAnimation(View viewToAnimate, int position) {
        // If the bound view wasn't previously displayed on screen, it's animated
        if (context != null && position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, android.R.anim.slide_in_left);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }
}

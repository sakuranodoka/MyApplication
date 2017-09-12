package invoice;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Configuration;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewStub;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.parceler.Parcels;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.logging.Handler;
import java.util.logging.LogRecord;

import autocomplete.InstantAutocomplete;
import intent.IntentKeycode;
import invoice.item.InvoiceBaseItem;
import invoice.item.ItemInvoice;
import invoice.item.ItemInvoiceDateDAO;
import invoice.item.ItemInvoiceDateDropdown;
import invoice.item.ItemInvoicePreview;
import invoice.item.ParcelBill;
import invoice.item.ParcelInvoice;
import invoice.viewholder.InvoiceContentViewHolder;
import invoice.viewholder.InvoiceHeaderViewHolder;
import invoice.viewholder.ProgressBarViewHolder;
import location.LocationData;
import location.pojo.GeoCoderPOJO;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit.ServiceRetrofit;
import retrofit2.Retrofit;
import seller.titlebar.SellerTitleDAO;

public class FragmentInvoiceDetail extends Fragment {

   private RecyclerView recyclerView;
   private InvoiceDetailAdapter adapter;
   public static final int INVOICE_CONTENT_HEADER = 0;
   public static final int INVOICE_CONTENT_VIEW = 1;
	public static final int INVOICE_CONTENT_LOADER = 2;

	private static int limited = 0;

	private static boolean isloading = false;

	private static boolean canloadmore = true;

	// let inside data
	private List<InvoiceBaseItem> listItem = new ArrayList<>();

   private Bundle b = null;

	private InterfaceInvoiceInfo interfaceInvoiceInfo = null;

	private final InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {

			isloading = false;

			if(listItem.size() != 0) {
				listItem.remove(listItem.size() - 1);
				adapter.notifyDataSetChanged();
			}

			ParcelInvoice pi = new ParcelInvoice();

			//List<InvoicePOJO> pojoList = (List<InvoicePOJO>) data;
			List<BillPOJO> pojoList = (List<BillPOJO>) data;

			//ArrayList<ItemInvoicePreview> listInvoice = new ArrayList<>();
			for (BillPOJO o : pojoList) {

				/*ItemInvoicePreview temp = new ItemInvoicePreview();
				temp.setInvoicePreview(i.getInfoInvoice());
				temp.setInvoiceSublocality(i.getInfoSubLocality());
				temp.setInvoiceLocality(i.getInfoLocality());
				temp.setInvoiceDate(i.getInfoTime());
				listInvoice.add(temp);*/

				ItemInvoice item = new ItemInvoice(INVOICE_CONTENT_VIEW);
				item.setBillPOJO(o);

				//item.setBILL_NO(i.getBILL_NO());

				/*if(i.getInfoInvoice().equals("0000")) {
					canloadmore = false;
					item.setInvoicePreview("ไม่พบข้อมูลเพิ่มเติม ...2");
					item.setInvoiceLocality("-");
					item.setInvoiceSubLocality("-");
					item.setInvoiceDate("ไม่บันทึกเวลา");

					int limits = Integer.parseInt(b.getString(InvoiceData.INVOICE_LIMIT));
					b.putString(InvoiceData.INVOICE_LIMIT, (limits-15)+"");
					item.setType(INVOICE_CONTENT_LOADER);
				} else {

					item.setInvoicePreview(i.getInfoInvoice());
					item.setInvoiceLocality(i.getInfoLocality());
					item.setInvoiceSubLocality(i.getInfoSubLocality());
					item.setInvoiceDate(i.getInfoTime());
				}*/
				listItem.add(item);
			}
			adapter.notifyDataSetChanged();
		}

		@Override
		public void onBodyError(ResponseBody responseBodyError) {}

		@Override
		public void onBodyErrorIsNull() {}

		@Override
		public void onFailure(Throwable t) {}
	};

   /*private final String DAY_NOW = "TODAY";
   private final String DAY_7 = "D7";
   private final String DAY_15 = "D15";
   private final String DAY_30 = "D30";
   private final String DAY_ALL = "ALL";
	private final String DAY_TAG = "DAY_TAG";
   private final String INVOICE_DATE_OPTIONAL = "\n" +
            "[\n" +
            "    {\n" +
            "      \"title\" : \"เฉพาะวันนี้\"\n," +
            "\"id\" : \"" + DAY_NOW + "\"" +
            "    },{\n" +
            "      \"title\" : \"7 วันย้อนหลัง\"\n," +
            "\"id\" : \"" + DAY_7 + "\"" +
            "    },{\n" +
            "      \"title\" : \"15 วันย้อนหลัง\"\n," +
            "\"id\" : \"" + DAY_15 + "\"" +
            "    },{\n" +
            "      \"title\" : \"30 วันย้อนหลัง\"\n," +
            "\"id\" : \"" + DAY_30 + "\"" +
            "    },{\n" +
            "      \"title\" : \"แสดงทั้งหมด\"\n," +
            "\"id\" : \"" + DAY_ALL + "\"" +
            "    }\n" +
            "]\n";*/

    public FragmentInvoiceDetail() {
        super();
    }

    public FragmentInvoiceDetail(Bundle b, InterfaceInvoiceInfo interfaceInvoiceInfo) {
		 super();

	    // Set Load more
	    this.canloadmore = true;
	    this.isloading = false;

		 this.b = b;
		 //if(this.b != null && !this.b.containsKey(InvoiceData.INVOICE_DAY_TAG))
	    //this.b.putString(InvoiceData.INVOICE_DAY_TAG, DAY_NOW);
		 this.interfaceInvoiceInfo = interfaceInvoiceInfo;

	    listItem = new ArrayList<>();

	    ParcelBill pb = null;
	    if(b.containsKey(InvoiceData.INVOICE_PARCEL_CONTENT)) {
		    pb = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL_CONTENT));
	    }

	    for(BillPOJO o : pb.getListBill()) {
		     ItemInvoice item = new ItemInvoice(INVOICE_CONTENT_VIEW);
		     item.setBillPOJO(o);

		     listItem.add(item);
	    }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.layout_recycler_view, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViews);

//        listItem = new ArrayList<>();
//        if(b != null) {
//	         /*if(b.containsKey(InvoiceData.INVOICE_INFO_TAG) && b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_WITH_USER_ID) {
//		         Gson gson = new Gson();
//		         List<ItemInvoiceDateDAO> listDateDAO = new ArrayList<>();
//		         try {
//			         Type listType = new TypeToken<ArrayList<ItemInvoiceDateDAO>>() {
//			         }.getType();
//			         listDateDAO = gson.fromJson(INVOICE_DATE_OPTIONAL, listType);
//		         } catch (Exception e) {
//			         e.printStackTrace();
//			         Log.e("error", "Parse Failed GSON" + e.getMessage());
//		         }
//		         ItemInvoiceDateDropdown dropdown = new ItemInvoiceDateDropdown(INVOICE_CONTENT_HEADER);
//		         dropdown.setDateTag(listDateDAO);
//
//		         //listItem.add(dropdown);
//	         }*/
//
//	        ParcelBill pb = null;
//	        if(b.containsKey(InvoiceData.INVOICE_PARCEL_CONTENT)) {
//		        pb = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL_CONTENT));
//	        }
//
//	        /*if(pb == null && b.containsKey(InvoiceData.INVOICE_PARCEL)) {
//		        isDataNull = false;
//		        pb = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL));
//	        }*/
//
//	         /*ParcelInvoice pi = null;
//            if(b.containsKey(InvoiceData.INVOICE_PARCEL_CONTENT)) {
//	            // แยก Parcel เพราะว่ากลัวซ้ำกับ list ที่เลือก ใน dialog ดูมั่วๆๆๆๆ หะ 5555
//					isDataNull = false;
//	            pi = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL_CONTENT));
//            }
//
//            if(pi == null && b.containsKey(InvoiceData.INVOICE_PARCEL)) {
//					isDataNull = false;
//	            pi = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL));
//            }*/
//
//            //if(!isDataNull) {
//                //for(ItemInvoicePreview i : pi.getListInvoice()) {
//            for(BillPOJO o : pb.getListBill()) {
//                 /*if(i.getInvoicePreview().equals("0000")) {
//	                 canloadmore = false;
//					  }*/
//
//                ItemInvoice item = new ItemInvoice(INVOICE_CONTENT_VIEW);
//
//	             item.setBillPOJO(o);
//                //item.setBILL_NO(o.getBILL_NO());
//
//						/*if(i.getInvoicePreview().equals("0000")) {
//							 //canloadmore = false;
//							 item.setInvoicePreview("ไม่พบข้อมูลเพิ่มเติม ...1");
//							 item.setInvoiceLocality("-");
//							 item.setInvoiceSubLocality("-");
//							 item.setInvoiceDate("ไม่บันทึกเวลา");
//						} else {
//
//							 item.setInvoicePreview(i.getInvoicePreview());
//							 item.setInvoiceLocality(i.getInvoiceLocality());
//							 item.setInvoiceSubLocality(i.getInvoiceSublocality());
//							 item.setInvoiceDate(i.getInvoiceDate());
//						}*/
//
//
//                 /*item.setInvoicePreview(i.getInvoicePreview());
//                 item.setInvoiceLocality(i.getInvoiceLocality());
//                 item.setInvoiceSubLocality(i.getInvoiceSublocality());
//                 item.setInvoiceDate(i.getInvoiceDate());*/
//                 listItem.add(item);
//             }
//            //}
//        } else {
//	        Log.e("BundleInFragmentIsNull", "true");
//        }

        adapter = new InvoiceDetailAdapter(listItem);
        recyclerView.setAdapter(adapter);

        int orientation = this.getResources().getConfiguration().orientation;
        if( orientation == Configuration.ORIENTATION_PORTRAIT ) {
            //code for portrait mode (แนวตั้ง)
	         LinearLayoutManager lm = new LinearLayoutManager(getContext());
            recyclerView.setLayoutManager(lm);
        } else {
            //code for landscape mode (แนวนอน)
            final GridLayoutManager gridLayoutManager = new GridLayoutManager(this.getActivity(), 2);
            gridLayoutManager.setSpanSizeLookup(new GridLayoutManager.SpanSizeLookup() {
                @Override
                public int getSpanSize(int position) {
                    switch(adapter.getItemViewType(position)) {
                        case 0:return 2;
                        default:return 1;
                    }
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        }

	    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			 private boolean isUserScrolling = false;
			 private boolean isListGoingUp = true;

			 @Override
			 public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				 super.onScrollStateChanged(recyclerView, newState);
				 //detect is the topmost item visible and is user scrolling? if true then only execute
				 //Log.e("SCROLL", "TRUE");
				 if (newState == RecyclerView.SCROLL_STATE_DRAGGING && !isloading) {
					 isUserScrolling = true;
					 if(isListGoingUp) {
						 if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
							 final LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();
							 if (lm.findLastCompletelyVisibleItemPosition()  == listItem.size()-1) {
								 android.os.Handler handler = new android.os.Handler();

								 if (isListGoingUp) {
									 if (lm.findLastCompletelyVisibleItemPosition()  == listItem.size()-1) {
										 Toast.makeText(getContext(),"กำลังประมวลผล ... ", Toast.LENGTH_SHORT).show();
										 InvoiceBaseItem temp = new InvoiceBaseItem(INVOICE_CONTENT_LOADER);

										 listItem.add(temp);

										 adapter.notifyDataSetChanged();

										 limited = limited + 15;

										 isloading = true;

										 handler.postDelayed(new Runnable() {
											 @Override
											 public void run() {
												 if(canloadmore) {
													 b.putString(InvoiceData.INVOICE_LIMIT, limited+"");
													 //async();
												 }
											 }
									   },500);
								   }
								 }
							 }
						 } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
							 final GridLayoutManager lm = (GridLayoutManager) recyclerView.getLayoutManager();
							 if (lm.findLastCompletelyVisibleItemPosition()  == listItem.size()-1) {
								 android.os.Handler handler = new android.os.Handler();
								 handler.postDelayed(new Runnable() {
									 @Override
									 public void run() {
										 if (isListGoingUp) {
											 if (lm.findLastCompletelyVisibleItemPosition()  == listItem.size()-1) {
												 Toast.makeText(getContext(),"ำลังประมวลผล ... ", Toast.LENGTH_SHORT).show();
												 InvoiceBaseItem temp = new InvoiceBaseItem(INVOICE_CONTENT_LOADER);

												 listItem.add(temp);

												 adapter.notifyDataSetChanged();

												 limited = limited + 15;

												 isloading = true;

												 if(canloadmore) {
													 b.putString(InvoiceData.INVOICE_LIMIT, limited+"");
													 //async();
												 }
											 }
										 }
									 }
								 },50);
							 }
						 }
					 }
				 }
			 }

		    @Override
		    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			    super.onScrolled(recyclerView, dx, dy);
			    if(isUserScrolling) {
				    if(dy > 0) {
					    //means user finger is moving up but the list is going down
					    isListGoingUp = true;
				    } else {
					    //means user finger is moving down but the list is going up
					    isListGoingUp = false;
				    }
			    }
		    }
	    });
	    adapter.notifyDataSetChanged();

       return rootView;
    }

    public class InvoiceDetailAdapter extends RecyclerView.Adapter {

        InvoiceDetailAdapter() {}
        InvoiceDetailAdapter(List<InvoiceBaseItem> listItem) { this.listItem = listItem; }

        public List<InvoiceBaseItem> listItem = new ArrayList<>();

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            if(viewType == INVOICE_CONTENT_HEADER) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.view_invoice_header, parent, false);
                return new InvoiceHeaderViewHolder(view);
            } else if(viewType == INVOICE_CONTENT_VIEW) {
                View view = LayoutInflater.from(getContext()).inflate(R.layout.view_invoice_info, parent, false);
                return new InvoiceContentViewHolder(view);
            } else if(viewType == INVOICE_CONTENT_LOADER) {
	            View view = LayoutInflater.from(getContext()).inflate(R.layout.layout_progressbar, parent, false);
	            return new ProgressBarViewHolder(view);
            }
            return null;
        }

       @Override
       public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
            if(!listItem.isEmpty()) {
                /*if(holder instanceof InvoiceHeaderViewHolder) {

                    final ItemInvoiceDateDropdown item = (ItemInvoiceDateDropdown) listItem.get(position);
                    final InvoiceHeaderViewHolder vh = (InvoiceHeaderViewHolder) holder;

	                 if(b != null && b.containsKey(InvoiceData.INVOICE_DAY_TAG)) {
		                 for(ItemInvoiceDateDAO daoItem : item.getDateTag()) {
		                     if(b.getString(InvoiceData.INVOICE_DAY_TAG).equals( daoItem.getId())) {
			                     vh.dropdownCustomDate.setText(daoItem.getTitle());
			                     break;
		                     }
		                 }
	                 }

                    ArrayAdapter<ItemInvoiceDateDAO> dropdownData = new ArrayAdapter<>(getContext(), android.R.layout.select_dialog_singlechoice, item.getDateTag());
                    vh.dropdownCustomDate.setAdapter(dropdownData);
                    vh.dropdownCustomDate.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            vh.dropdownCustomDate.showDropDown();
                        }
                    });

	                vh.dropdownCustomDate.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                         @Override
                         public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//	                         int size = listItem.size();
//	                         if (size > 0) {
//		                         for (int i = 0; i < size; i++) {
//			                         listItem.remove(0);
//		                         }
//		                         notifyItemRangeRemoved(0, size);
//	                         }
	                        interfaceInvoiceInfo.onOptionalChange(item.getDateTag().get(position).getId());
                         }
	                });

                } else */
                if(holder instanceof InvoiceContentViewHolder) {
						final ItemInvoice item = (ItemInvoice) listItem.get(position);

	               final BillPOJO temp = item.getBillPOJO();

						InvoiceContentViewHolder vh = (InvoiceContentViewHolder) holder;
						TextView textViewInvoicePreview = (TextView) vh.textViewInvoicePreview;
						//textViewInvoicePreview.setText(item.getInvoicePreview());
	                //textViewInvoicePreview.setText(item.getBILL_NO());

	                textViewInvoicePreview.setText(temp.getBILL_NO());

	                //temp.setBILL_NO(i.getBILL_NO());

						TextView textViewInvoiceDate = (TextView) vh.textViewInvoiceDate;
						//textViewInvoiceDate.setText(item.getInvoiceDate());
	                textViewInvoiceDate.setText(temp.getBILL_DATE());

	               TextView textViewInvoiceBoxNumber = (TextView) vh.textViewInvoiceBoxNumber;
	               if(textViewInvoiceBoxNumber != null && temp.getTOTAL_BOX() != null) {
		               textViewInvoiceBoxNumber.setText(temp.getBILL_COUNT()+" / "+Integer.parseInt(temp.getTOTAL_BOX()));
	               }

						if(b != null) {
							//if(b.containsKey(InvoiceData.INVOICE_INFO_TAG) && b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_WITH_USER_ID) {
		                  ImageButton btnRemove = (ImageButton) vh.btnRemove;
		                  btnRemove.setVisibility(View.GONE);

								TextView textViewInvoiceAddress = (TextView) vh.textViewInvoiceAddress;
								textViewInvoiceAddress.setVisibility(View.GONE);
								//textViewInvoiceAddress.setText(item.getInvoiceSubLocality()+" "+item.getInvoiceLocality());

								RelativeLayout layout = (RelativeLayout) vh.relativeInvoiceInfo;
								layout.setOnClickListener(new View.OnClickListener() {
									@Override
									public void onClick(View v) {
										Bundle b = new Bundle();
										//b.putString(InvoiceData.INVOICE_SCANNER_STRING, item.getInvoicePreview());
										b.putString(InvoiceData.INVOICE_SCANNER_STRING, temp.getBILL_NO());
										b.putInt(InvoiceData.BILL_COUNT, Integer.parseInt(temp.getBILL_COUNT()));
										b.putInt(InvoiceData.TOTAL_BOX, Integer.parseInt(temp.getTOTAL_BOX()));

										b.putInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION, position);

										interfaceInvoiceInfo.onBarcodeScan(b);
									}
								});

	                  /*} else if(b.containsKey(InvoiceData.INVOICE_INFO_TAG) && b.getInt(InvoiceData.INVOICE_INFO_TAG) == InvoiceData.INVOICE_INFO_INNER_APP) {
//								TextView textViewInvoiceAddress = (TextView) vh.textViewInvoiceAddress;
//								textViewInvoiceAddress.setVisibility(View.GONE);
							}*/
						}
                } else if(holder instanceof ProgressBarViewHolder) {
	                ProgressBarViewHolder vh = (ProgressBarViewHolder) holder;
	                vh.avi.smoothToShow();
                }
            }
        }

	    @Override
        public int getItemCount() {
            if(this.listItem != null) {
                return this.listItem.size();
            } else {
                return 0;
            }
        }

        @Override
        public int getItemViewType(int position) {
            if(this.listItem != null) {
                return this.listItem.get(position).getType();
            } else {
                return 0;
            }
        }

        public BillPOJO getPOJO(int position) {
	        if(this.listItem != null) {
		        ItemInvoice item = (ItemInvoice) listItem.get(position);
		        return item.getBillPOJO();
	        } else return null;
        }
    }

	protected void async() {
		if(this.b != null) {
			//b.putString(InvoiceData.INVOICE_LIMIT, limited+"");
			new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_PRE_INVOICE, b);
		}
	}

	public void increaseCounting(int position, int BILL_COUNT) {
		try {
			final ItemInvoice item = (ItemInvoice) this.listItem.get(position);
			BillPOJO temp = item.getBillPOJO();
			temp.setBILL_COUNT(BILL_COUNT+"");

			item.setBillPOJO(temp);

			adapter.notifyDataSetChanged();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public void removeByPosition(int position) {
		try {

			ArrayList<InvoiceBaseItem> copy = new ArrayList<InvoiceBaseItem>(listItem);
			copy.remove(position);

			listItem.clear();
			listItem.addAll(copy);

			adapter.notifyDataSetChanged();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}

	public BillPOJO getBILLPOJO(int position) {
		if(this.adapter == null) {
			// please set new adapter, the old adapter was เดี้ยงไปแล้ว
			Log.e("TheyreAllOfDead", listItem.size()+"");
			if(this.listItem != null) {
				ItemInvoice item = (ItemInvoice) listItem.get(position);
				return item.getBillPOJO();
			} else return null;
		}
		else return this.adapter.getPOJO(position);
	}

	public void clearance() {
		limited = 0;
		canloadmore = true;
		isloading = false;
		listItem.clear();
		if(adapter != null)
			adapter.notifyDataSetChanged();
	}
}

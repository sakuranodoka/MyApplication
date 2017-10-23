package invoice;

import android.content.res.Configuration;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.myapplication.BusProvider;
import com.example.administrator.myapplication.R;

import org.parceler.Parcels;

import java.util.ArrayList;
import java.util.List;

import invoice.item.InvoiceBaseItem;
import invoice.item.ItemInvoice;
import invoice.item.ParcelBill;
import invoice.viewholder.InvoiceContentViewHolder;
import invoice.viewholder.InvoiceHeaderViewHolder;
import invoice.viewholder.ProgressBarViewHolder;
import okhttp3.ResponseBody;
import retrofit.InterfaceListen;
import retrofit.RetrofitAbstract;
import retrofit2.Retrofit;

public class FragmentInvoiceDetail extends Fragment {

   private RecyclerView recyclerView;
   private InvoiceDetailAdapter adapter;

   public static final int INVOICE_CONTENT_HEADER = 0;
   public static final int INVOICE_CONTENT_VIEW = 1;
	public static final int INVOICE_CONTENT_LOADER = 2;

	private static int limited = 0;

	private static boolean canloadmore = true;

	// let inside data
	private List<InvoiceBaseItem> listItem = new ArrayList<>();

   private Bundle b = null;

	private ParcelBill pb = null;

	private InterfaceInvoiceInfo interfaceInvoiceInfo = null;

	private final InterfaceListen interfaceListen = new InterfaceListen() {
		@Override
		public void onResponse(Object data, Retrofit retrofit) {

			if (listItem.size() != 0) {
				 listItem.remove(listItem.size() - 1);
				 adapter.notifyDataSetChanged();
			}
			canloadmore = true;

			ParcelBill pb = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL_CONTENT));

			ArrayList<BillPOJO> listbill = pb.getListBill();

			List<BillPOJO> pojoList = (List<BillPOJO>) data;

			for (BillPOJO i : pojoList) {
				BillPOJO temp = new BillPOJO();

				temp.setBILL_NO(i.getBILL_NO());
				temp.setBILL_DATE(i.getBILL_DATE());
				temp.setNET_AMOUNT(i.getNET_AMOUNT());
				temp.setTOTAL_BOX(i.getTOTAL_BOX());
				temp.setBILL_COUNT(i.getBILL_COUNT());

				ItemInvoice item = new ItemInvoice(INVOICE_CONTENT_VIEW);
				item.setBillPOJO(temp);

				listItem.add(item);
			}

			listbill.addAll((pojoList));
			pb.setListBill(listbill);

			b.putParcelable(InvoiceData.INVOICE_PARCEL_CONTENT, Parcels.wrap(pb));

			adapter.notifyDataSetChanged();
		}

		@Override
		public void onBodyError(ResponseBody responseBodyError) {}

		@Override
		public void onBodyErrorIsNull() {}

		@Override
		public void onFailure(Throwable t) {}
	};

   public FragmentInvoiceDetail() {
       super();
   }

   public FragmentInvoiceDetail(Bundle b, InterfaceInvoiceInfo interfaceInvoiceInfo) {
		 super();

	    // Set Load more
	    this.canloadmore = true;

		 this.b = b;

		 this.interfaceInvoiceInfo = interfaceInvoiceInfo;

	    listItem = new ArrayList<>();

	    ParcelBill pb = null;
	    if(b.containsKey(InvoiceData.INVOICE_PARCEL_CONTENT)) {
		    pb = Parcels.unwrap(b.getParcelable(InvoiceData.INVOICE_PARCEL_CONTENT));
	    }

	    if(pb != null) {
		    for (BillPOJO o : pb.getListBill()) {
			    ItemInvoice item = new ItemInvoice(INVOICE_CONTENT_VIEW);
			    item.setBillPOJO(o);

			    listItem.add(item);
		    }
	    }
    }

	public FragmentInvoiceDetail(ParcelBill pb) {
		super();
		this.pb = pb;

		listItem = new ArrayList<>();

		if (pb != null) {

			 for (BillPOJO o : pb.getListBill()) {
				 ItemInvoice item = new ItemInvoice(INVOICE_CONTENT_VIEW);
				 item.setBillPOJO(o);

				 listItem.add(item);
			}
		}

		this.limited = 0;
		this.canloadmore = true;

		if (!BusProvider.isBusNull()) {
			 Log.e("IS_LIMIT_POST", "true");

			 LimitWrapper wrapper = new LimitWrapper();
			 wrapper.setLimit(0);

			 BusProvider.getInstance().post(wrapper);
		} else {
			Log.e("IS_LIMIT_POST", "false");
		}
	}

	public void fixedLimited(int limited) {
		this.limited = limited;

		LimitWrapper wrapper = new LimitWrapper();
		wrapper.setLimit(limited);

		if(!BusProvider.isBusNull()) BusProvider.getInstance().post(wrapper);
	}

   public void setData(ParcelBill pb) {

		 if (pb != null) {

			  // Remove preloader
			  if (listItem.size() != 0) {
				   listItem.remove(listItem.size() - 1);
				   //adapter.notifyDataSetChanged();
			  }

			  this.canloadmore = true;

			  for (BillPOJO o : pb.getListBill()) {
				  ItemInvoice item = new ItemInvoice(INVOICE_CONTENT_VIEW);
				  item.setBillPOJO(o);
				  listItem.add(item);
			  }
			  adapter.notifyDataSetChanged();
		  }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        setRetainInstance(true);

        View rootView = inflater.inflate(R.layout.layout_recycler_view, container, false);

        recyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerViews);

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
                    switch (adapter.getItemViewType(position)) {
                         case 0 : return 2;
                         default : return 1;
                    }
                }
            });
            recyclerView.setLayoutManager(gridLayoutManager);
        }

	    recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
			 @Override
			 public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
				 super.onScrollStateChanged(recyclerView, newState);
				 LinearLayoutManager lm = (LinearLayoutManager) recyclerView.getLayoutManager();

				 if (lm.findLastCompletelyVisibleItemPosition() == listItem.size()-1 && newState == 0 && canloadmore == true) {
					  // Bottom Detected
					  canloadmore = false;

					 if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
						  android.os.Handler handler = new android.os.Handler();
						  Toast.makeText(getContext(),"โหลดข้อมูลบิลล์ ... ", Toast.LENGTH_SHORT).show();
						  InvoiceBaseItem temp = new InvoiceBaseItem(INVOICE_CONTENT_LOADER);

						  listItem.add(temp);

						  adapter.notifyDataSetChanged();

						  recyclerView.scrollToPosition(listItem.size() - 1);

						  handler.postDelayed(new Runnable() {
							  @Override
							  public void run() {

							 	 limited = limited + 15;
								 async();

							  }
						  },500);
					 } else if (recyclerView.getLayoutManager() instanceof GridLayoutManager) {
						 android.os.Handler handler = new android.os.Handler();

						 Toast.makeText(getContext(),"โหลดข้อมูลบิลล์ ... ", Toast.LENGTH_SHORT).show();
						 InvoiceBaseItem temp = new InvoiceBaseItem(INVOICE_CONTENT_LOADER);

						 listItem.add(temp);

						 adapter.notifyDataSetChanged();

						 // select to bottom
						 recyclerView.scrollToPosition(listItem.size() - 1);

						 handler.postDelayed(new Runnable() {
							 @Override
							 public void run() {
								 limited = limited + 15;
								 async();
							 }
						 }, 500);
					 }
				 }
			 }

		    @Override
		    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
			    super.onScrolled(recyclerView, dx, dy);
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
                if (holder instanceof InvoiceContentViewHolder) {
	                 final ItemInvoice item = (ItemInvoice) listItem.get(position);

		              final BillPOJO temp = item.getBillPOJO();

						  InvoiceContentViewHolder vh = (InvoiceContentViewHolder) holder;
	                 TextView textViewInvoicePreview = (TextView) vh.textViewInvoicePreview;

		              textViewInvoicePreview.setText(temp.getBILL_NO());

	                 TextView textViewInvoiceDate = (TextView) vh.textViewInvoiceDate;
		              textViewInvoiceDate.setText(temp.getBILL_DATE());

		              TextView textViewInvoiceBoxNumber = (TextView) vh.textViewInvoiceBoxNumber;
		              if (textViewInvoiceBoxNumber != null && temp.getTOTAL_BOX() != null) {
			               textViewInvoiceBoxNumber.setText(temp.getBILL_COUNT()+" / "+Integer.parseInt(temp.getTOTAL_BOX()));
		              }

	                 ImageButton btnRemove = (ImageButton) vh.btnRemove;
	                 btnRemove.setVisibility(View.GONE);

	                 TextView textViewInvoiceAddress = (TextView) vh.textViewInvoiceAddress;
	                 textViewInvoiceAddress.setVisibility(View.GONE);

	                 RelativeLayout layout = (RelativeLayout) vh.relativeInvoiceInfo;
	                 layout.setOnClickListener(new View.OnClickListener() {
								@Override
								public void onClick(View v) {
								Bundle instanceBundle = new Bundle();

//								instanceBundle.putString(InvoiceData.INVOICE_SCANNER_STRING, temp.getBILL_NO());
//								instanceBundle.putInt(InvoiceData.BILL_COUNT, Integer.parseInt(temp.getBILL_COUNT()));
//								instanceBundle.putInt(InvoiceData.TOTAL_BOX, Integer.parseInt(temp.getTOTAL_BOX()));
//								instanceBundle.putInt(InvoiceData.SHARED_PREFERENCES_BILL_POSITION, position);

								BarcodeWrapper wrapper = new BarcodeWrapper();
								wrapper.setBillPOJO(temp);
								wrapper.setPosition(position);

								if (!BusProvider.isBusNull()) {
									 BusProvider.getInstance().post(wrapper);
								}

								//interfaceInvoiceInfo.onBarcodeScan(b);
								}
							});
						//}
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
	        if (this.listItem != null) {
		         ItemInvoice item = (ItemInvoice) listItem.get(position);
		         return item.getBillPOJO();
	        } else return null;
        }
    }

	 protected void async() {
		/*if(this.b != null) {
			b.putString(InvoiceData.INVOICE_LIMIT, limited+"");
			new ServiceRetrofit().callServer(interfaceListen, RetrofitAbstract.RETROFIT_PRE_INVOICE, b);
		}*/

		Log.e("(Async)LIMIT ", limited+" (When pan down)");

		Bundle instanceBundle = new Bundle();

		instanceBundle.putString(InvoiceData.INVOICE_LIMIT, limited+"");

	   if (!BusProvider.isBusNull()) {
		    LimitWrapper limitWrapper = new LimitWrapper();
		    limitWrapper.setLimit(limited);
		    BusProvider.getInstance().post(limitWrapper);
		}

		AsynchronousWrapper wrapper = new AsynchronousWrapper();
		wrapper.setRequired(true);
		wrapper.setRetrofitAbstractLayer(RetrofitAbstract.RETROFIT_PRE_INVOICE);
		wrapper.setInstanceBundle(instanceBundle);

		if (!BusProvider.isBusNull()) {
			 BusProvider.getInstance().post(wrapper);
		}
	}

	public void goToPosition(final int position) {
		if (adapter != null && this.listItem.size() >= position) {
			 recyclerView.getLayoutManager().scrollToPosition(position);
			 adapter.notifyDataSetChanged();
		}
	}

	public void increaseCounting(int position) {

		try {

			final ItemInvoice item = (ItemInvoice) this.listItem.get(position);

			BillPOJO pojo = item.getBillPOJO();

			int BILL_COUNT = Integer.parseInt(pojo.getBILL_COUNT());

			BILL_COUNT = BILL_COUNT+1;

			pojo.setBILL_COUNT(BILL_COUNT+"");

			item.setBillPOJO(pojo);

			recyclerView.getLayoutManager().scrollToPosition(position);

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

		//if (this.adapter == null) {
			// please set new adapter, the old adapter was เดี้ยงไปแล้ว
		 if (this.listItem != null) {
			 Log.e("AfterScanPositionBBB", position+"");
			  ItemInvoice item = (ItemInvoice) listItem.get(position);
			  return item.getBillPOJO();
		 } else return null;
		/*} else {
			if (this.listItem != null) {
				ItemInvoice item = (ItemInvoice) listItem.get(position);
				return item.getBillPOJO();
			} else return null;
		}*/
		//else return this.adapter.getPOJO(position);
	}

	public ParcelBill getParcelBill() {
		ParcelBill genereatedP = new ParcelBill();

		ArrayList<BillPOJO> listBill = new ArrayList<>();
		for (int i = 0 ; i < this.listItem.size() ; i++) {
			if (this.listItem.get(i) instanceof  ItemInvoice) {
				 ItemInvoice item = (ItemInvoice) this.listItem.get(i);
				 if (item.getType() == INVOICE_CONTENT_VIEW) {
				     listBill.add(item.getBillPOJO());
				 }
			}
		}
		genereatedP.setListBill(listBill);
		return genereatedP;
	}

	public void setNewLimited() {
		limited = 0;
	}
}

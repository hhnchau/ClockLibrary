package chauhuynh.info.clock_library.clock;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import chauhuynh.info.clock_library.R;

/**
 * Created by appro on 09/03/2018.
 */

public class Clock {
    private static Clock Instance = null;

    public static Clock getInstance() {
        if (Instance == null) {
            Instance = new Clock();
        }
        return Instance;
    }

    private List<NumberClock> listFrom;
    private List<NumberClock> listTo;
    private int isScroll = 0;

    public void show(Context context, int[] overnight, int[] cinejoy, int[] position, boolean currentDay, int numberHour, final CallbackClock callbackClock) {
        if (context != null && context instanceof Activity && !((Activity) context).isFinishing()) {
            final Dialog dialog = new Dialog(context, R.style.dialog_full_transparent_background);
            dialog.setOwnerActivity((Activity) context);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setContentView(R.layout.item_clock);
            Window window = dialog.getWindow();
            if (window != null) {
                WindowManager.LayoutParams wlp = window.getAttributes();
                wlp.gravity = Gravity.CENTER;
                window.setAttributes(wlp);
                dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
                dialog.show();

                // FIND VIEW BY ID
                dialog.findViewById(R.id.dialog_clock).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        dialog.dismiss();
                    }
                });

                final TextView tvFrom = dialog.findViewById(R.id.tv_from);
                final TextView tvTo = dialog.findViewById(R.id.tv_to);
                ListView lvFrom = dialog.findViewById(R.id.lv_from);
                ListView lvTo = dialog.findViewById(R.id.lv_to);

                listFrom = new ArrayList<>();
                setupListViewFrom(context, lvFrom, lvTo, tvFrom, tvTo, overnight, cinejoy, position, currentDay, numberHour);

                listTo = new ArrayList<>();
                int type = NumberClock.ODD;
                if (HelperClock.isEven(tvFrom.getText().toString())) {
                    type = NumberClock.EVEN;
                }

                int[] p = new int[2];
                p[1] = position[0] + (numberHour * 2);
                setupListViewTo(context, type, lvFrom, lvTo, tvFrom, tvTo, overnight, cinejoy, p, currentDay, numberHour);

                dialog.findViewById(R.id.tv_clock_ok).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        callbackClock.onValue(tvFrom.getText().toString(), tvTo.getText().toString());
                        dialog.dismiss();
                    }
                });

            }
        }
    }

    private void getListNumber(List<NumberClock> list) {
        int from = HelperClock.getSystemHour() * 2;

        for (int i = 0; i < 48; i++) {
            NumberClock numberClock = new NumberClock();
            if (i >= from) {
                numberClock.setEnable(true);
            } else {
                numberClock.setEnable(false);
            }
            numberClock.setNumber(parseNumber(i));

            list.add(numberClock);
        }
    }

    private void getListNumberEven(List<NumberClock> list) {
        for (int i = 0; i < 48; i++) {
            NumberClock numberClock = new NumberClock();
            if (i % 2 == 0) {
                numberClock.setEnable(true);
            } else {
                numberClock.setEnable(false);
            }
            numberClock.setNumber(parseNumber(i));

            list.add(numberClock);
        }
    }

    private void getListNumberOdd(List<NumberClock> list) {
        for (int i = 0; i < 48; i++) {
            NumberClock numberClock = new NumberClock();
            if (i % 2 > 0) {
                numberClock.setEnable(true);
            } else {
                numberClock.setEnable(false);
            }
            numberClock.setNumber(parseNumber(i));

            list.add(numberClock);
        }
    }

    private void getListNumberOvernight(List<NumberClock> list, int[] limit) {
        int start = limit[0] * 2;
        int end = limit[1] * 2;


        int more=7;
        for (int i = 0; i < list.size(); i++) {
            if (start > end) {
                if (i < end || i > start) {
                    list.get(i).setEnable(false);
                }
                more = 48-start;
            } else {
                if (i >= start && i <= end) {
                    list.get(i).setEnable(false);
                }
                more = 48-end;
            }

        }

        for (int i = 0; i <= 7 - more; i++){
            NumberClock numberClock = new NumberClock();
            numberClock.setNumber("");
            list.add(numberClock);
        }
    }

    private void getListNumberCineJoy(List<NumberClock> list, int[] limit) {
        int start = limit[0] * 2;
        int end = limit[1] * 2;

        for (int i = 0; i < list.size(); i++) {
            if (i <= start || i >= end) {
                list.get(i).setEnable(false);
            }
        }
    }

    private String parseNumber(int i) {
        int hour = i / 2;
        int min = i % 2;

        String number;
        if (min > 0) {
            number = hour + ":30";
        } else {
            number = hour + ":00";
        }

        if (hour < 10) {
            number = "0" + number;
        }

        return number;
    }

    private void setupListViewFrom(final Context context, final ListView lvFrom, final ListView lvTo, final TextView tvFrom, final TextView tvTo, final int[] overnight, final int[] cinejoy, final int[] position, final boolean currentDay, final int numberHour) {

        getListNumber(listFrom);
        getListNumberOvernight(listFrom, overnight);
        if (cinejoy.length > 1) {
            int[] cine = new int[2];
            cine[0] = cinejoy[0];
            cine[1] = cinejoy[1];
            getListNumberCineJoy(listFrom, cine);
        }

        AdapterClock adapterClockFrom = new AdapterClock(context, listFrom);
        lvFrom.setAdapter(adapterClockFrom);
        lvFrom.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScroll = 1;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listFrom.get(firstVisibleItem).isEnable()) {
                    String s = listFrom.get(firstVisibleItem).getNumber();
                    tvFrom.setText(s);

                    if (isScroll == 1) {
                        listTo = new ArrayList<>();
                        int type = NumberClock.ODD;
                        if (HelperClock.isEven(s)) {
                            type = NumberClock.EVEN;
                        }

                        int[] p = new int[2];
                        p[1] = (HelperClock.handleLimit(s)) + (numberHour * 2);
                        setupListViewTo(context, type, lvFrom, lvTo, tvFrom, tvTo, overnight, cinejoy, p, currentDay, numberHour);
                    }
                }

                //if (isScroll == 1)
                //updateListViewTo(lvTo, tvFrom.getText().toString(), tvTo.getText().toString());

            }
        });

        lvFrom.setSelection(position[0]);
    }

    private void setupListViewTo(Context context, int type, final ListView lvFrom, final ListView lvTo, final TextView tvFrom, final TextView tvTo, int[] overnight, int[] cinejoy, final int[] position, boolean currentDay, final int numberHour) {
        if (type == NumberClock.EVEN) {
            getListNumberEven(listTo);
        } else {
            getListNumberOdd(listTo);
        }
        getListNumberOvernight(listTo, overnight);
        if (cinejoy.length > 3) {
            int[] cine = new int[2];
            cine[0] = cinejoy[2];
            cine[1] = cinejoy[3];
            getListNumberCineJoy(listTo, cine);
        }

        AdapterClock adapterClockTo = new AdapterClock(context, listTo);
        lvTo.setAdapter(adapterClockTo);
        lvTo.setOnScrollListener(new AbsListView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                isScroll = 2;
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
                if (listTo.get(firstVisibleItem).isEnable()) {
                    tvTo.setText(listTo.get(firstVisibleItem).getNumber());
                }
                if (isScroll == 2)
                    updateListViewFrom(lvFrom, tvFrom.getText().toString(), tvTo.getText().toString(), numberHour);
            }
        });

        lvTo.setSelection(position[1]);
    }

    private void updateListViewTo(ListView lv, String from, String to, int numberHour) {
        isScroll = 0;
        int f = HelperClock.handleLimit(from);
        int t = HelperClock.handleLimit(to);

        if (f >= t - 1) {
            lv.setSelection(f + (numberHour * 2));
        }
    }

    private void updateListViewFrom(ListView lv, String from, String to, int numberHour) {
        isScroll = 0;
        int f = HelperClock.handleLimit(from);
        int t = HelperClock.handleLimit(to);

        if (t <= f + numberHour) {
            lv.setSelection(t - (numberHour * 2));
        }
    }
}

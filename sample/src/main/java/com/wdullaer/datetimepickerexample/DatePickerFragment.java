package com.wdullaer.datetimepickerexample;

import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;

import com.wdullaer.materialdatetimepicker.JalaliCalendar;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;
import java.util.Locale;

/**
 * A simple {@link Fragment} subclass.
 */
public class DatePickerFragment extends Fragment implements DatePickerDialog.OnDateSetListener {

    private TextView dateTextView;
    private CheckBox modeDarkDate;
    private CheckBox modeCustomAccentDate;
    private CheckBox vibrateDate;
    private CheckBox dismissDate;
    private CheckBox titleDate;
    private CheckBox showYearFirst;
    private CheckBox showVersion2;
    private CheckBox switchOrientation;
    private CheckBox limitSelectableDays;
    private CheckBox highlightDays;
    private CheckBox defaultSelection;
    private DatePickerDialog dpd;
    Typeface font;

    DatePickerDialog.Type calendarType;

    public DatePickerFragment() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.datepicker_layout, container, false);

        // Find our View instances
        dateTextView = view.findViewById(R.id.date_textview);
        Button dateButton = view.findViewById(R.id.date_button);
        modeDarkDate = view.findViewById(R.id.mode_dark_date);
        modeCustomAccentDate = view.findViewById(R.id.mode_custom_accent_date);
        vibrateDate = view.findViewById(R.id.vibrate_date);
        dismissDate = view.findViewById(R.id.dismiss_date);
        titleDate = view.findViewById(R.id.title_date);
        showYearFirst = view.findViewById(R.id.show_year_first);
        showVersion2 = view.findViewById(R.id.show_version_2);
        switchOrientation = view.findViewById(R.id.switch_orientation);
        limitSelectableDays = view.findViewById(R.id.limit_dates);
        highlightDays = view.findViewById(R.id.highlight_dates);
        defaultSelection = view.findViewById(R.id.default_selection);

        final Spinner spinner = view.findViewById(R.id.calendar_type);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.calendar_types_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        font = Typeface.createFromAsset(getActivity().getAssets(), "IRANSansMobile(FaNum).ttf");

        // Android original DatePickerDialog
//        view.findViewById(R.id.original_button).setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Calendar now = Calendar.getInstance();
//                new android.app.DatePickerDialog(
//                        getActivity(),
//                        new android.app.DatePickerDialog.OnDateSetListener() {
//                            @Override
//                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
//                                Log.d("Orignal", "Got clicked");
//                            }
//                        },
//                        now.get(Calendar.YEAR),
//                        now.get(Calendar.MONTH),
//                        now.get(Calendar.DAY_OF_MONTH)
//                ).show();
//            }
//        });

        // Show a datepicker when the dateButton is clicked
        dateButton.setOnClickListener(v -> {
            Calendar now = null;
            if (defaultSelection.isChecked()) {
                now.add(Calendar.DATE, 7);
            }
            /*
            It is recommended to always create a new instance whenever you need to show a Dialog.
            The sample app is reusing them because it is useful when looking for regressions
            during testing
             */

            if(spinner.getSelectedItemPosition()==0)
                calendarType = DatePickerDialog.Type.JALALI;
            else
                calendarType = DatePickerDialog.Type.GREGORIAN;


//                Toast.makeText(getContext(), calendarType.toString(), Toast.LENGTH_SHORT).show();

            switch (calendarType){
                case GREGORIAN:
                    now = Calendar.getInstance();
                    break;

                case JALALI:
                    now = JalaliCalendar.getInstance();
                    break;
            }

            if (dpd == null) {
                dpd = DatePickerDialog.newInstance(
                        calendarType,
                        DatePickerFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            } else {
                dpd.setCalendarType(calendarType);
                dpd.initialize(
                        DatePickerFragment.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
            }
            switch (calendarType){
                case GREGORIAN:
                    dpd.setFont(null);
                    break;

                case JALALI:
                    dpd.setFont(font);
                    break;
            }
            dpd.setThemeDark(modeDarkDate.isChecked());
            dpd.vibrate(vibrateDate.isChecked());
            dpd.dismissOnPause(dismissDate.isChecked());
            dpd.showYearPickerFirst(showYearFirst.isChecked());
            dpd.setVersion(showVersion2.isChecked() ? DatePickerDialog.Version.VERSION_2 : DatePickerDialog.Version.VERSION_1);
            if (modeCustomAccentDate.isChecked()) {
                dpd.setAccentColor(Color.parseColor("#9C27B0"));
            }
            if (titleDate.isChecked()) {
                switch (calendarType){
                    case GREGORIAN:
                        dpd.setTitle("DatePicker Title");
                        break;
                    case JALALI:
                        dpd.setTitle("عنوان انتخابگر تاریخ");
                        break;
                }
            }
            if (highlightDays.isChecked()) {
                Calendar date1 = Calendar.getInstance();
                Calendar date2 = Calendar.getInstance();
                date2.add(Calendar.WEEK_OF_MONTH, -1);
                Calendar date3 = Calendar.getInstance();
                date3.add(Calendar.WEEK_OF_MONTH, 1);
                Calendar[] days = {date1, date2, date3};
                dpd.setHighlightedDays(days);
            }
            if (limitSelectableDays.isChecked()) {
                Calendar[] days = new Calendar[13];
                for (int i = -6; i < 7; i++) {
                    Calendar day;
                    switch (calendarType){
                        case JALALI:
                            day = JalaliCalendar.getInstance();
                            break;

                        default:
                            day = Calendar.getInstance();
                            break;
                    }
                    day.add(Calendar.DAY_OF_MONTH, i * 2);
                    days[i + 6] = day;
                }
                dpd.setSelectableDays(days);
            }
            if (switchOrientation.isChecked()) {
                if (dpd.getVersion() == DatePickerDialog.Version.VERSION_1) {
                    dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.HORIZONTAL);
                } else {
                    dpd.setScrollOrientation(DatePickerDialog.ScrollOrientation.VERTICAL);
                }
            }
            dpd.setOnCancelListener(dialog -> {
                Log.d("DatePickerDialog", "Dialog was cancelled");
                dpd = null;
            });
            dpd.show(requireFragmentManager(), "Datepickerdialog");
        });

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        dpd = null;
    }

    @Override
    public void onResume() {
        super.onResume();
        DatePickerDialog dpd = (DatePickerDialog) requireFragmentManager().findFragmentByTag("Datepickerdialog");
        if(dpd != null) dpd.setOnDateSetListener(this);
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        String date = "You picked the following date: "+dayOfMonth+"/"+(++monthOfYear)+"/"+year;
        dateTextView.setText(date);
        dpd = null;
    }
}

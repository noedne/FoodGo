package bney.foodgo;

import android.util.Log;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

/**
 * Created by bney on 5/4/2017.
 */

public class ParseSource {
    public ArrayList<DiningLocation> parse(String source) {
        ArrayList<DiningLocation> locs = new ArrayList<>();
        String word = "Today's Hours:";
        int index = 0;
        SimpleDateFormat dateFormat = new SimpleDateFormat("h:mm aa");
        while ((index = source.indexOf(word, index)) >= 0) {
            int start = source.indexOf(":", index + word.length());
            start = source.lastIndexOf(" ", start) + 1;
            index = source.indexOf("M", start) + 1;
            index = source.indexOf("M", index) + 1;
            String[] times = source.substring(start, index).split(" - ");
            Date[] dates = new Date[2];
            try {
                dates[0] = dateFormat.parse(times[0]);
                dates[1] = dateFormat.parse(times[1]);
            } catch (ParseException e) {
                e.printStackTrace();
            }
            GregorianCalendar now = new GregorianCalendar();
            GregorianCalendar begin = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), dates[0].getHours(), dates[0].getMinutes(), dates[0].getSeconds());
            GregorianCalendar end = new GregorianCalendar(now.get(Calendar.YEAR), now.get(Calendar.MONTH), now.get(Calendar.DAY_OF_MONTH), dates[1].getHours(), dates[1].getMinutes(), dates[1].getSeconds());
            long lnow = now.getTimeInMillis();
            long lbegin = begin.getTimeInMillis();
            long lend = end.getTimeInMillis();
            if (!(lbegin < lend && lbegin < lnow && lnow < lend || lend < lbegin && (lnow < lend || lbegin < lnow)))
                continue;
            index = source.lastIndexOf("</h3>", index);
            start = source.lastIndexOf(">", index);
            String name = source.substring(start + 1, index);
            Log.d("LOCINFO:", name);
            start = source.indexOf("/40", index) + 1;
            index = source.indexOf("/", start);
            String[] strs = source.substring(start, index).split(",");
            index = source.indexOf(word, index) + word.length();
            double[] nums = new double[2];
            nums[0] = Double.parseDouble(strs[0]);
            Log.d("LOCINFO:", ""+nums[0]);
            nums[1] = Double.parseDouble(strs[1]);
            Log.d("LOCINFO:", ""+nums[1]);
            locs.add(new DiningLocation(name, nums));
        }
        return locs;
    }
}

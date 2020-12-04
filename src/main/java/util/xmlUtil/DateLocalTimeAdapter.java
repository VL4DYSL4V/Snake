package util.xmlUtil;

import javax.xml.bind.annotation.adapters.XmlAdapter;
import java.time.*;
import java.util.Date;

public class DateLocalTimeAdapter extends XmlAdapter<Date, LocalTime> {


    @Override
    public LocalTime unmarshal(Date v) {
        return LocalDateTime.ofInstant(v.toInstant(),
                ZoneId.systemDefault()).toLocalTime();
    }

    @Override
    public Date marshal(LocalTime v) throws Exception {
        LocalDateTime now = LocalDateTime.now();
        Instant instant = v.atDate(LocalDate.of(now.getYear(), now.getMonth(), now.getDayOfMonth())).
                atZone(ZoneId.systemDefault()).toInstant();
        return Date.from(instant);
    }
}

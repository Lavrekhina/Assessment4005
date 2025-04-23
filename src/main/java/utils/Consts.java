package utils;

import java.util.Collection;
import java.util.List;

public class Consts {
    public static final Collection<String> ORDER_STATUSES = List.of("Cancelled", "Created", "Prepared", "Ready for shipment");
    public static final Collection<String> LOCATIONS = List.of(
            "London",
            "Manchester",
            "Birmingham",
            "Glasgow",
            "Liverpool",
            "Bristol",
            "Leeds",
            "Sheffield",
            "Edinburgh",
            "Cardiff",
            "Newcastle upon Tyne",
            "Belfast",
            "Leicester",
            "Nottingham",
            "Southampton",
            "Aberdeen",
            "Portsmouth",
            "Brighton",
            "Plymouth",
            "Reading",
            "Middlesbrough",
            "Derby",
            "Norwich",
            "Swansea",
            "York",
            "Cambridge",
            "Oxford",
            "Exeter",
            "Bath",
            "Chester",
            "Canterbury",
            "Inverness",
            "Dundee",
            "Stoke-on-Trent",
            "Coventry",
            "Wolverhampton",
            "Sunderland",
            "Bradford",
            "Hull",
            "Luton",
            "Milton Keynes"
    );
    public static final Collection<String> SHIPMENT_STATUSES = List.of("Delivered", "Dispatched", "In transit", "Canceled");
    public static final List<Integer> PAGE_SIZES = List.of(20, 30, 50, 100, 200);
}

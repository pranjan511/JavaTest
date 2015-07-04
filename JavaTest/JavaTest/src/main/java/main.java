import option.GeolocationOption;
import option.GeolocationOptionException;
import option.GeolocationOptionService;

import java.util.List;

public class main {

    public static void main(String[] args) {
        if (args.length == 0 || args.length >1) {
            System.err.println("Enter at least an argument");
            System.exit(1);
        }

        String QueryString = args[0];

        GeolocationOptionService service = new GeolocationOptionService();
        service.setTarget("http://api.goeuro.com/api/v2");

        try {
            List<GeolocationOption> options = service.getGeolocationOption(QueryString);

            String csv = service.exportAsCSV(options);

            System.out.print(csv);
        } catch (GeolocationOptionException e) {
            System.err.println("Error: " + e.getMessage());
            System.exit(2);
        } finally {
            service.shutdown();
        }
    }
}

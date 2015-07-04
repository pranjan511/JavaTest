package option;
import java.util.*;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.dataformat.csv.CsvMapper;
import com.fasterxml.jackson.dataformat.csv.CsvSchema;
import com.google.common.base.Function;
import com.google.common.base.Preconditions;
import com.google.common.base.Predicate;
import com.google.common.collect.Collections2;
import com.mashape.unirest.http.HttpResponse;
import com.mashape.unirest.http.Unirest;
import tool.Mime;

import java.io.IOException;

public class GeolocationOptionService {

    public String Target;

    /**
     * Queries remote location for option and returns them
     *
     * @param query text query
     * @return list of Geolocation option  for query
     * @throws GeolocationOptionException some exception occurs querying from remote server
     */
    public List<GeolocationOption> getGeolocationOption(String query) throws GeolocationOptionException {
        Preconditions.checkArgument(query != null);

        final ObjectMapper jsonMapper = new ObjectMapper();

        try {
            HttpResponse<String> response = Unirest.get(Target + "/position/suggest/en/" + query)
                    .header("Accept", Mime.JSON.getMimeType())
                    .asString();

            return Arrays.asList(jsonMapper.readValue(response.getRawBody(), GeolocationOption[].class));
        } catch (Exception e) {
            throw new GeolocationOptionException(e.getMessage(), e);
        }
    }

    /**
     * Exports options list as CSV with configured csvWriter
     *
     * @param GeolocationOption iterable of options
     * @return CSV o/p as string
     */
    public String exportAsCSV(Collection<GeolocationOption> GeolocationOption) {
        Preconditions.checkArgument(GeolocationOption != null);

        final ObjectWriter csvWriter = new CsvMapper().writer(
                CsvSchema.builder()
                        .setUseHeader(true)
                        .addColumn("_type")
                        .addColumn("_id")
                        .addColumn("name")
                        .addColumn("type")
                        .addColumn("latitude")
                        .addColumn("longitude")
                        .build());

        try {
            return csvWriter.writeValueAsString(Collections2.filter(Collections2.transform(GeolocationOption, new Function<GeolocationOption, Object>() {
                @Override
                public Object apply(final GeolocationOption GeolocationOption) {
                    try {
                        return new Object() {
                            public String _type = GeolocationOption.get_type();
                            public long _id = GeolocationOption.get_id();
                            public String name = GeolocationOption.getName();
                            public String type = GeolocationOption.getType();
                            public double latitude = GeolocationOption.getLocation().getLatitude();
                            public double longitude = GeolocationOption.getLocation().getLongitude();
                        };
                    } catch (Exception e) {
                        System.err.print(e.getMessage());
                        return null; // filtered out below
                    }
                }
            }), new Predicate<Object>() {
                @Override
                public boolean apply(Object csvDto) {
                    return csvDto != null;
                }
            }));
        } catch (Exception e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    public void shutdown() {

        try {
            Unirest.shutdown();
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }

    }

    public void setTarget(String Target) {
        this.Target = Target;
    }
}

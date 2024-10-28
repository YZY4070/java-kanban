package http.handlers.specific.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class LocalDateTimeTypeGsonAdapter extends TypeAdapter<LocalDateTime> {

  private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm:ss");

  @Override
  public void write(JsonWriter writer, final LocalDateTime time) throws IOException {
    if (time != null) {
      writer.value(time.format(FORMATTER));
    } else {
      writer.value((String) null);
    }

  }

  @Override
  public LocalDateTime read(final JsonReader reader) throws IOException {
    return LocalDateTime.parse(reader.nextString(), FORMATTER);
  }
}
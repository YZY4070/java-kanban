package http.handlers.specific.utils;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import java.io.IOException;
import java.time.Duration;

public class DurationTypeGsonAdapter extends TypeAdapter<Duration> {

  @Override
  public void write(final JsonWriter writer, final Duration duration) throws IOException {
    if (duration != null){
      writer.value(duration.toMinutes());
    }else {
      writer.value(Duration.ZERO.toMinutes());
    }
  }

  @Override
  public Duration read(JsonReader reader) throws IOException {
    return Duration.ofMinutes(reader.nextInt());
  }
}
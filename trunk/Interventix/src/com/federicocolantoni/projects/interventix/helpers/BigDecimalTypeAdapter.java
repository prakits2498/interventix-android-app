package com.federicocolantoni.projects.interventix.helpers;

import java.io.IOException;
import java.math.BigDecimal;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;

public class BigDecimalTypeAdapter extends TypeAdapter<BigDecimal> {

    @Override
    public BigDecimal read(JsonReader reader) throws IOException {

	return new BigDecimal(reader.nextString());
    }

    @Override
    public void write(JsonWriter writer, BigDecimal value) throws IOException {

	writer.value(value);
    }
}

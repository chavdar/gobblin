/* (c) 2014 LinkedIn Corp. All rights reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not use
 * this file except in compliance with the License. You may obtain a copy of the
 * License at  http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software distributed
 * under the License is distributed on an "AS IS" BASIS, WITHOUT WARRANTIES OR
 * CONDITIONS OF ANY KIND, either express or implied.
 */

package gobblin.converter.avro;

import gobblin.converter.Converter;
import gobblin.converter.SchemaConversionException;
import gobblin.converter.SingleRecordIterable;
import org.apache.avro.Schema;
import org.apache.avro.generic.GenericRecord;

import com.google.common.base.Optional;
import com.google.common.base.Preconditions;

import gobblin.configuration.ConfigurationKeys;
import gobblin.configuration.WorkUnitState;
import gobblin.converter.DataConversionException;
import gobblin.converter.EmptyIterable;
import gobblin.util.AvroUtils;


/**
 * A converter class where the input is an Avro record, and the output is a specific field in that record. Since the
 * field can be of any type this Converter returns a Java {@link Object}. The parameter converter.avro.extractor.field.path
 * specifies the location of the field to retrieve. Nested fields can be specified by following use the following
 * syntax: field.nestedField
 */
public class AvroFieldRetrieverConverter extends Converter<Schema, Schema, GenericRecord, Object> {

  private String fieldLocation;

  @Override
  public Converter<Schema, Schema, GenericRecord, Object> init(WorkUnitState workUnit) {
    Preconditions.checkArgument(workUnit.contains(ConfigurationKeys.CONVERTER_AVRO_EXTRACTOR_FIELD_PATH),
        "Missing required property converter.avro.extractor.field.path for the AvroFieldRetrieverConverter class.");
    this.fieldLocation = workUnit.getProp(ConfigurationKeys.CONVERTER_AVRO_EXTRACTOR_FIELD_PATH);
    return this;
  }

  @Override
  public Schema convertSchema(Schema inputSchema, WorkUnitState workUnit) throws SchemaConversionException {
    return AvroUtils.getFieldSchema(inputSchema, this.fieldLocation).orNull();
  }

  @Override
  public Iterable<Object> convertRecord(Schema outputSchema, GenericRecord inputRecord, WorkUnitState workUnit)
      throws DataConversionException {
    Optional<Object> record = AvroUtils.getFieldValue(inputRecord, this.fieldLocation);
    return record.isPresent() ? new SingleRecordIterable<Object>(record.get()) : new EmptyIterable<Object>();
  }
}

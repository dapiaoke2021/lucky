package io.renren.common.serializer;

import cn.hutool.core.util.ObjectUtil;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import io.renren.common.base.interfaces.BaseEnum;
import org.springframework.stereotype.Component;

import java.io.IOException;

/**
 * @author liuyuchan286
 * @email liuyuchan286@gmail.com
 * @date 2020-05-18 16:11
 */
@Component
public class EnumWebSerializer extends JsonSerializer<BaseEnum> {


    @Override
    public void serialize(BaseEnum baseEnum, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        if (ObjectUtil.isNull(baseEnum)) {
            jsonGenerator.writeNull();
        }else {
            jsonGenerator.writeObject(baseEnum.getKey());
            jsonGenerator.writeFieldName(jsonGenerator.getOutputContext().getCurrentName()+"Str");
            jsonGenerator.writeString(baseEnum.getValue());
        }


    }

}

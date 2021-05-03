package de.team7.swt.domain.infrastructure.money;

import com.fasterxml.jackson.core.util.VersionUtil;
import com.fasterxml.jackson.databind.deser.Deserializers;
import com.fasterxml.jackson.databind.module.SimpleDeserializers;
import com.fasterxml.jackson.databind.module.SimpleModule;
import com.fasterxml.jackson.databind.module.SimpleSerializers;
import com.fasterxml.jackson.databind.ser.Serializers;
import org.javamoney.moneta.FastMoney;
import org.javamoney.moneta.Money;
import org.javamoney.moneta.RoundedMoney;
import org.javamoney.moneta.spi.FastMoneyAmountFactory;
import org.javamoney.moneta.spi.MoneyAmountFactory;
import org.javamoney.moneta.spi.RoundedMoneyAmountFactory;
import org.springframework.stereotype.Component;

import javax.money.CurrencyUnit;
import javax.money.MonetaryAmount;

/**
 * {@link com.fasterxml.jackson.databind.Module} registering {@link com.fasterxml.jackson.databind.JsonSerializer}s and
 * {@link com.fasterxml.jackson.databind.JsonDeserializer}s for reading or writing {@link MonetaryAmount} types to or
 * from a JSON tree.
 *
 * @author Vincent Nadoll
 */
@Component
public class MoneyModule extends SimpleModule {

    public MoneyModule() {
        super(MoneyModule.class.getSimpleName(), VersionUtil.packageVersionFor(MoneyModule.class));
    }

    @Override
    public void setupModule(SetupContext context) {
        Serializers serializers = setupSerializers();
        Deserializers deserializers = setupDeserializers();

        context.addSerializers(serializers);
        context.addDeserializers(deserializers);
    }

    private Serializers setupSerializers() {
        SimpleSerializers serializers = new SimpleSerializers();
        serializers.addSerializer(CurrencyUnit.class, new CurrencyUnitSerializer());
        serializers.addSerializer(MonetaryAmount.class, new MonetaryAmountSerializer());
        return serializers;
    }

    private Deserializers setupDeserializers() {
        SimpleDeserializers deserializers = new SimpleDeserializers();
        deserializers.addDeserializer(CurrencyUnit.class, new CurrencyUnitDeserializer());
        deserializers.addDeserializer(MonetaryAmount.class, new MonetaryAmountDeserializer<>(new MoneyAmountFactory()));
        deserializers.addDeserializer(Money.class, new MonetaryAmountDeserializer<>(new MoneyAmountFactory()));
        deserializers.addDeserializer(FastMoney.class, new MonetaryAmountDeserializer<>(new FastMoneyAmountFactory()));
        deserializers.addDeserializer(RoundedMoney.class, new MonetaryAmountDeserializer<>(new RoundedMoneyAmountFactory()));
        return deserializers;
    }
}

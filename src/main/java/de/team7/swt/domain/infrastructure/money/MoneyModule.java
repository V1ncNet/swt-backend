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

        CurrencyUnitSerializer cuSerializer = new CurrencyUnitSerializer();
        serializers.addSerializer(CurrencyUnit.class, cuSerializer);

        MonetaryAmountSerializer maSerializer = new MonetaryAmountSerializer();
        serializers.addSerializer(MonetaryAmount.class, maSerializer);

        return serializers;
    }

    private Deserializers setupDeserializers() {
        SimpleDeserializers deserializers = new SimpleDeserializers();

        CurrencyUnitDeserializer cuDeserializer = new CurrencyUnitDeserializer();
        deserializers.addDeserializer(CurrencyUnit.class, cuDeserializer);

        MoneyAmountFactory maFactory = new MoneyAmountFactory();
        MonetaryAmountDeserializer<Money> maDeserializer = new MonetaryAmountDeserializer<>(maFactory);
        deserializers.addDeserializer(MonetaryAmount.class, maDeserializer);
        deserializers.addDeserializer(Money.class, maDeserializer);

        FastMoneyAmountFactory fmFactory = new FastMoneyAmountFactory();
        MonetaryAmountDeserializer<FastMoney> fmDeserializer = new MonetaryAmountDeserializer<>(fmFactory);
        deserializers.addDeserializer(FastMoney.class, fmDeserializer);

        RoundedMoneyAmountFactory rmFactory = new RoundedMoneyAmountFactory();
        MonetaryAmountDeserializer<RoundedMoney> rmDeserializer = new MonetaryAmountDeserializer<>(rmFactory);
        deserializers.addDeserializer(RoundedMoney.class, rmDeserializer);

        return deserializers;
    }
}

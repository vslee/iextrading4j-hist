package pl.zankowski.iextrading4j.hist.api.message.administrative;

import pl.zankowski.iextrading4j.hist.api.field.IEXPrice;
import pl.zankowski.iextrading4j.hist.api.message.IEXMessage;
import pl.zankowski.iextrading4j.hist.api.message.administrative.field.IEXLULDTier;
import pl.zankowski.iextrading4j.hist.api.util.IEXByteConverter;

import java.util.Arrays;
import java.util.Objects;

import static pl.zankowski.iextrading4j.hist.api.IEXMessageType.SECURITY_DIRECTORY;

public class IEXSecurityDirectoryMessage extends IEXMessage {

    private final byte securityDirectoryFlag;
    private final long timestamp;
    private final String symbol;
    private final int roundLotSize;
    private final IEXPrice adjustedPOCPrice;
    private final IEXLULDTier luldTier;

    private IEXSecurityDirectoryMessage(
            final byte securityDirectoryFlag,
            final long timestamp,
            final String symbol,
            final int roundLotSize,
            final IEXPrice adjustedPOCPrice,
            final IEXLULDTier luldTier) {
        super(SECURITY_DIRECTORY);
        this.securityDirectoryFlag = securityDirectoryFlag;
        this.timestamp = timestamp;
        this.symbol = symbol;
        this.roundLotSize = roundLotSize;
        this.adjustedPOCPrice = adjustedPOCPrice;
        this.luldTier = luldTier;
    }

    public boolean isTestSecurity() {
        return (securityDirectoryFlag & 0x80) != 0;
    }

    public boolean isWhenIssuedSecurity() {
        return (securityDirectoryFlag & 0x40) != 0;
    }

    public boolean isETP() {
        return (securityDirectoryFlag & 0x20) != 0;
    }

    public long getTimestamp() {
        return timestamp;
    }

    public String getSymbol() {
        return symbol;
    }

    public int getRoundLotSize() {
        return roundLotSize;
    }

    public IEXPrice getAdjustedPOCPrice() {
        return adjustedPOCPrice;
    }

    public IEXLULDTier getLuldTier() {
        return luldTier;
    }

    public static IEXSecurityDirectoryMessage createIEXMessage(final byte[] bytes) {
        final byte iexSecurityDirectoryFlag = bytes[1];
        final long timestamp = IEXByteConverter.convertBytesToLong(Arrays.copyOfRange(bytes, 2, 10));
        final String symbol = IEXByteConverter.convertBytesToString(Arrays.copyOfRange(bytes, 10, 18));
        final int roundLotSize = IEXByteConverter.convertBytesToInt(Arrays.copyOfRange(bytes, 18, 22));
        final IEXPrice adjustedPOCPrice = IEXByteConverter.convertBytesToIEXPrice(Arrays.copyOfRange(bytes, 22, 30));
        final IEXLULDTier iexluldTier = IEXLULDTier.getLULDTier(bytes[30]);

        return new IEXSecurityDirectoryMessage(iexSecurityDirectoryFlag, timestamp, symbol, roundLotSize,
                adjustedPOCPrice, iexluldTier);
    }

    @Override
    public boolean equals(final Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        final IEXSecurityDirectoryMessage that = (IEXSecurityDirectoryMessage) o;
        return timestamp == that.timestamp &&
                roundLotSize == that.roundLotSize &&
                securityDirectoryFlag == that.securityDirectoryFlag &&
                Objects.equals(symbol, that.symbol) &&
                Objects.equals(adjustedPOCPrice, that.adjustedPOCPrice) &&
                luldTier == that.luldTier;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), securityDirectoryFlag, timestamp, symbol, roundLotSize, adjustedPOCPrice, luldTier);
    }

    @Override
    public String toString() {
        return "IEXSecurityDirectoryMessage{" +
                "securityDirectoryFlag=" + securityDirectoryFlag +
                ", timestamp=" + timestamp +
                ", symbol='" + symbol + '\'' +
                ", roundLotSize=" + roundLotSize +
                ", adjustedPOCPrice=" + adjustedPOCPrice +
                ", luldTier=" + luldTier +
                "} " + super.toString();
    }
}

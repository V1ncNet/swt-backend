package de.team7.swt.checkout.application;

import com.fasterxml.jackson.annotation.JsonUnwrapped;
import de.team7.swt.checkout.model.LineItem;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Value;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import java.util.Objects;

/**
 * Value object encapsulating a {@link LineItem}'s completion status and optional error message.
 *
 * @author Vincent Nadoll
 * @see OrderCompletionReport
 */
@Value
@AllArgsConstructor(access = AccessLevel.PACKAGE)
public class LineItemCompletion {

    @JsonUnwrapped
    LineItem item;

    CompletionStatus status;

    @Nullable
    String message;

    /**
     * Creates a new completion indicating a successful verification of the given {@link LineItem}.
     *
     * @param item must not be {@literal null}
     * @return a new completion
     */
    public static LineItemCompletion success(LineItem item) {
        Assert.notNull(item, "Line item must not be null");
        return new LineItemCompletion(item, CompletionStatus.SUCCEEDED, null);
    }

    /**
     * Creates a new completion indicating a skipped verification of the given {@link LineItem}.
     *
     * @param item must not be {@literal null}
     * @return a new completion
     */
    public static LineItemCompletion skip(LineItem item) {
        Assert.notNull(item, "Line item must not be null");
        return new LineItemCompletion(item, CompletionStatus.SUCCEEDED, "The line item has been skipped.");
    }

    /**
     * Creates a new completion indicating a erroneous verification of the given {@link LineItem}.
     *
     * @param item must not be {@literal null}
     * @return a new completion
     */
    public static LineItemCompletion error(LineItem item, String message) {
        Assert.notNull(item, "Line item must not be null");
        return new LineItemCompletion(item, CompletionStatus.FAILED, message);
    }

    /**
     * Indicates whether this completion has failed.
     *
     * @return {@literal true} if this completion is successful; {@literal false} otherwise
     */
    public boolean hasFailed() {
        return Objects.equals(CompletionStatus.FAILED, status);
    }
}

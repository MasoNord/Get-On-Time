package org.masonord.delivery.controller.v1.request;

import lombok.Getter;
import org.masonord.delivery.enums.ExceptionType;
import org.masonord.delivery.exception.ExceptionHandler;
import org.springframework.beans.factory.annotation.Autowired;

@Getter
public class OffsetBasedPageRequest {
    @Autowired
    ExceptionHandler exceptionHandler;

    private int limit;
    private int offset;



    public OffsetBasedPageRequest(int offset, int limit)  {
        if (limit < 0) {
            throw exception("range", ExceptionType.RANGE_NOT_SATISFIABLE, Integer.toString(limit));
        }

        if (offset < 0) {
            throw exception("range", ExceptionType.RANGE_NOT_SATISFIABLE, Integer.toString(offset));
        }

        this.limit = limit;
        this.offset = offset;
    }

    public int getPageNumber() {
        return (int) (double) (offset / limit);
    }

    public int getPageSize() {
        return limit;
    }

    public OffsetBasedPageRequest next() {
        return new OffsetBasedPageRequest(getOffset() + getPageSize(), getPageSize());
    }

    public OffsetBasedPageRequest previous() {
        return hasPrevious() ? new OffsetBasedPageRequest(getOffset() - getPageSize(), getPageSize()) : this;
    }

    public OffsetBasedPageRequest first() {
        return new OffsetBasedPageRequest(0, getPageSize());
    }

    public OffsetBasedPageRequest prefiousOrFirst() {
        return hasPrevious() ? previous() : first();
    }

    private boolean hasPrevious() {
        return offset > limit;
    }

    public RuntimeException exception(String model, ExceptionType exceptionType, String ...args) {
        return exceptionHandler.throwException(model, exceptionType, args);
    }

}

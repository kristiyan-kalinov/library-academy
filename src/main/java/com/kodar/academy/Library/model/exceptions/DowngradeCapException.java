package com.kodar.academy.Library.model.exceptions;

import com.kodar.academy.Library.model.constants.Constants;

public class DowngradeCapException extends RuntimeException{

    public DowngradeCapException(String plan, int maxBooks) {
        super(String.format(Constants.CAP_DOWNGRADE_EXCEPTION_MSG, plan, maxBooks));
    }

}

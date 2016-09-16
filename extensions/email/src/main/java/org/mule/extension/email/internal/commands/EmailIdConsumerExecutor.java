/*
 * Copyright (c) MuleSoft, Inc.  All rights reserved.  http://www.mulesoft.com
 * The software in this package is published under the terms of the CPAL v1.0
 * license, a copy of which has been included with this distribution in the
 * LICENSE.txt file.
 */
package org.mule.extension.email.internal.commands;

import org.mule.extension.email.api.attributes.BaseEmailAttributes;
import org.mule.extension.email.api.exception.EmailException;
import org.mule.runtime.api.message.Message;
import org.mule.runtime.extension.api.runtime.operation.OperationResult;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

/**
 * Executes an operation that receives an emailId.
 * <p>
 * This class is the responsible for looking in the incoming message if there are one or more emailIds to accept and perform the
 * operation when no explicit emailId is specified.
 *
 * @since 4.0
 */
public class EmailIdConsumerExecutor {

  public static final String NO_ID_ERROR =
      "Expecting an explicit emailId value or email attributes in the incoming mule message in order to store an email.";

  /**
   * Receives {@link Consumer} operation to perform for an specific emailId.
   * <p>
   * if no emailId is specified, the operation will try to find one or more emails in the incoming {@link Message}.
   * <p>
   * If no explicit {@code emailId} is provided and this method is not able to find any emailIds in the incoming {@link Message}
   * the execution of the {@link Consumer} will fail.
   *
   * @param muleMessage the incoming {@link Message}.
   * @param emailId the id of the email that is wanted to perform the operation, can be {@code null}
   * @param consumer a {@link Consumer} to be applied to an email.
   */
  void execute(Message muleMessage, Integer emailId, Consumer<Integer> consumer) {
    if (emailId == null) {
      Object payload = muleMessage.getPayload().getValue();
      if (payload instanceof List) {
        for (Object o : (List) payload) {
          if (o instanceof OperationResult) {
            emailId = getIdOrFail(((OperationResult) o));
            consumer.accept(emailId);
          } else {
            throw new EmailException(NO_ID_ERROR);
          }
        }
        return;
      }
      Object result = muleMessage.getPayload().getValue();
      if (result instanceof OperationResult) {
        emailId = getIdOrFail((OperationResult) result);
      } else {
        throw new EmailException(NO_ID_ERROR);
      }
    }
    consumer.accept(emailId);
  }

  /**
   * Gets an emailId from a MuleMessage of fails if the MuleMessage does not contains attributes of {@link BaseEmailAttributes} type.
   */
  private int getIdOrFail(OperationResult result) {
    Optional attributes = result.getAttributes();
    if (attributes.isPresent()) {
      return ((BaseEmailAttributes) attributes.get()).getId();
    }
    throw new EmailException(NO_ID_ERROR);
  }
}
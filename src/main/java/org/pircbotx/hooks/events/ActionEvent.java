/**
 * Copyright (C) 2010-2014 Leon Blakey <lord.quackstar at gmail.com>
 *
 * This file is part of PircBotX.
 *
 * PircBotX is free software: you can redistribute it and/or modify it under the
 * terms of the GNU General Public License as published by the Free Software
 * Foundation, either version 3 of the License, or (at your option) any later
 * version.
 *
 * PircBotX is distributed in the hope that it will be useful, but WITHOUT ANY
 * WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS FOR
 * A PARTICULAR PURPOSE. See the GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License along with
 * PircBotX. If not, see <http://www.gnu.org/licenses/>.
 */
package org.pircbotx.hooks.events;

import javax.annotation.Nullable;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NonNull;
import org.pircbotx.Channel;
import org.pircbotx.PircBotX;
import org.pircbotx.User;
import org.pircbotx.UserHostmask;
import org.pircbotx.hooks.Event;
import org.pircbotx.hooks.types.GenericChannelUserEvent;
import org.pircbotx.hooks.types.GenericMessageEvent;

/**
 * Called whenever an ACTION is sent from a user. E.g. such events generated by
 * typing "/me goes shopping" in most IRC clients.
 *
 * @author Leon Blakey <lord.quackstar at gmail.com>
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class ActionEvent<T extends PircBotX> extends Event<T> implements GenericMessageEvent<T>, GenericChannelUserEvent<T> {
	/**
	 * The user hostmask that sent the action.
	 */
	@Getter(onMethod = @_(
			@Override))
	protected final UserHostmask userHostmask;
	/**
	 * The user that sent the action.
	 */
	@Getter(onMethod = @_(
			@Override,
			@Nullable))
	protected final User user;
	/**
	 * The channel that the action message was sent in. A value of
	 * <code>null</code> means that this is a private message, not a channel
	 */
	@Getter(onMethod = @_(
			@Override,
			@Nullable))
	protected final Channel channel;
	/**
	 * The action message.
	 */
	protected final String action;

	public ActionEvent(T bot, @NonNull UserHostmask userHostmask, User user, Channel channel, @NonNull String action) {
		super(bot);
		this.userHostmask = userHostmask;
		this.user = user;
		this.channel = channel;
		this.action = action;
	}

	/**
	 * Returns the action sent by the user. Same result as {@link #getAction() }
	 *
	 * @return Action sent by the user
	 */
	@Override
	public String getMessage() {
		return action;
	}

	/**
	 * Respond to an action with an action in either the channel that the
	 * message came from or a private message.
	 * <p>
	 * Example
	 * <pre>
	 *   * SomeUser thinks that this is awesome
	 *   * PircBotX agrees
	 * </pre>
	 *
	 * @param response The response to send
	 */
	@Override
	public void respond(@Nullable String response) {
		if (getChannel() == null)
			getUserHostmask().send().action(response);
		else
			getChannel().send().action(response);
	}
	
	/**
	 * Respond with a message to the channel without the prefix
	 * @param response The response to send
	 */
	public void respondChannel(@Nullable String response) {
		if(getChannel() == null)
			throw new RuntimeException("Event does not contain a channel");
		getChannel().send().message(response);
	}
	
	/**
	 * Respond with a PM directly to the user
	 * @param response The response to send
	 */
	public void respondPrivateMessage(@Nullable String response) {
		getUser().send().message(response);
	}
}

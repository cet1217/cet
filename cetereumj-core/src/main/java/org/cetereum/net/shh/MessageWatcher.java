/*
 * Copyright (c) [2016] [ <ceter.camp> ]
 * This file is part of the cetereumJ library.
 *
 * The cetereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The cetereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the cetereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package org.cetereum.net.shh;


import java.util.Arrays;

public abstract class MessageWatcher {
    private String to;
    private String from;
    private Topic[] topics = null;

    public MessageWatcher() {
    }

    public MessageWatcher(String to, String from, Topic[] topics) {
        this.to = to;
        this.from = from;
        this.topics = topics;
    }

    public MessageWatcher setTo(String to) {
        this.to = to;
        return this;
    }

    public MessageWatcher setFrom(String from) {
        this.from = from;
        return this;
    }

    public MessageWatcher setFilterTopics(Topic[] topics) {
        this.topics = topics;
        return this;
    }

    public String getTo() {
        return to;
    }

    public String getFrom() {
        return from;
    }

    public Topic[] getTopics() {
        return topics == null ? new Topic[0] : topics;
    }

    boolean match(String to, String from, Topic[] topics) {
        if (this.to != null) {
            if (!this.to.equals(to)) {
                return false;
            }
        }

        if (this.from != null) {
            if (!this.from.equals(from)) {
                return false;
            }
        }

        if (this.topics != null) {
            for (Topic watchTopic : this.topics) {
                for (Topic msgTopic : topics) {
                    if (watchTopic.equals(msgTopic)) return true;
                }
            }
            return false;
        }
        return true;
    }

    protected abstract void newMessage(WhisperMessage msg);
}

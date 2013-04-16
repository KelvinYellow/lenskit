/*
 * LensKit, an open source recommender systems toolkit.
 * Copyright 2010-2013 Regents of the University of Minnesota and contributors
 * Work on LensKit has been funded by the National Science Foundation under
 * grants IIS 05-34939, 08-08692, 08-12148, and 10-17697.
 *
 * This program is free software; you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as
 * published by the Free Software Foundation; either version 2.1 of the
 * License, or (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or FITNESS
 * FOR A PARTICULAR PURPOSE. See the GNU General Public License for more
 * details.
 *
 * You should have received a copy of the GNU General Public License along with
 * this program; if not, write to the Free Software Foundation, Inc., 51
 * Franklin Street, Fifth Floor, Boston, MA 02110-1301, USA.
 */
package org.grouplens.lenskit.eval;


import com.google.common.base.Preconditions;
import org.grouplens.lenskit.eval.config.EvalConfig;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;

/**
 * The abstract class of Command.
 *
 * @author Shuo Chang<schang@cs.umn.edu>
 */
public abstract class AbstractCommand<T> implements Command<T> {
    @Nullable protected String name;
    private EvalConfig config;

    public AbstractCommand(String name) {
        this.name = name;
    }

    public AbstractCommand<T> setConfig(@Nonnull EvalConfig cfg) {
        Preconditions.checkNotNull(cfg, "configuration cannot be null");
        config = cfg;
        return this;
    }

    @Nonnull
    public EvalConfig getConfig() {
        if (config == null) {
            throw new IllegalStateException("no configuration is specified");
        }
        return config;
    }

    public AbstractCommand<T> setName(String name) {
        this.name = name;
        return this;
    }

    /**
     * {@inheritDoc}
     * <p>This implementation returns the {@link #name} field, throwing
     * {@link IllegalStateException} if it is {@code null}.
     */
    @Override @Nonnull
    public String getName() {
        if (name == null) {
            throw new IllegalStateException("no name specified");
        } else {
            return name;
        }
    }

    @Override
    public abstract T call() throws CommandException;

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder();
        return sb.append(getClass().getSimpleName())
                 .append('(')
                 .append(getName())
                 .append(')')
                 .toString();
    }
}

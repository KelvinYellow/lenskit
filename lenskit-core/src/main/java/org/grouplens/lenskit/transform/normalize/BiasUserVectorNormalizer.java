/*
 * LensKit, an open source recommender systems toolkit.
 * Copyright 2010-2014 LensKit Contributors.  See CONTRIBUTORS.md.
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
package org.grouplens.lenskit.transform.normalize;

import it.unimi.dsi.fastutil.longs.Long2DoubleMap;
import org.grouplens.lenskit.vectors.MutableSparseVector;
import org.grouplens.lenskit.vectors.SparseVector;
import org.grouplens.lenskit.vectors.VectorEntry;
import org.lenskit.bias.BiasModel;

import javax.inject.Inject;

/**
 * Item vector normalizer that subtracts user-item biases.
 */
public class BiasUserVectorNormalizer extends AbstractUserVectorNormalizer {
    private final BiasModel model;

    @Inject
    public BiasUserVectorNormalizer(BiasModel bias) {
        model = bias;
    }

    @Override
    public VectorTransformation makeTransformation(long userId, SparseVector vector) {
        return new Transform(userId);
    }

    private class Transform implements VectorTransformation {
        private final long user;
        private final double userBias;

        public Transform(long uid) {
            user = uid;
            userBias = model.getIntercept() + model.getUserBias(user);
        }

        @Override
        public MutableSparseVector apply(MutableSparseVector vector) {
            Long2DoubleMap users = model.getItemBiases(vector.keySet());
            for (VectorEntry e: vector) {
                vector.set(e, e.getValue() - userBias - users.get(e.getKey()));
            }
            return vector;
        }

        @Override
        public MutableSparseVector unapply(MutableSparseVector vector) {
            Long2DoubleMap users = model.getItemBiases(vector.keySet());
            for (VectorEntry e: vector) {
                vector.set(e, e.getValue() + userBias + users.get(e.getKey()));
            }
            return vector;
        }

        @Override
        public double apply(long key, double value) {
            return value - userBias - model.getItemBias(key);
        }

        @Override
        public double unapply(long key, double value) {
            return value + userBias + model.getItemBias(key);
        }
    }
}

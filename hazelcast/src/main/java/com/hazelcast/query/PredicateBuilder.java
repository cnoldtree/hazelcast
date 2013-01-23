/*
 * Copyright (c) 2008-2013, Hazelcast, Inc. All Rights Reserved.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.hazelcast.query;

import com.hazelcast.core.MapEntry;
import com.hazelcast.query.impl.Predicates;

import java.util.ArrayList;
import java.util.List;

public class PredicateBuilder implements Predicate {
    public String attribute = null;
    List<Predicate> lsPredicates = new ArrayList<Predicate>();

    public boolean apply(MapEntry mapEntry) {
        return lsPredicates.get(0).apply(mapEntry);
    }

    public EntryObject getEntryObject() {
        return new EntryObject(this);
    }

    public PredicateBuilder and(Predicate predicate) {
        if (predicate != PredicateBuilder.this) {
            throw new RuntimeException("Illegal and statement expected: "
                    + PredicateBuilder.class.getSimpleName() + ", found: " +
                    ((predicate == null) ? "null" : predicate.getClass().getSimpleName()));
        }
        int index = lsPredicates.size() - 2;
        Predicate first = lsPredicates.remove(index);
        Predicate second = lsPredicates.remove(index);
        lsPredicates.add(Predicates.and(first, second));
        return this;
    }

    public PredicateBuilder or(Predicate predicate) {
        if (predicate != PredicateBuilder.this) {
            throw new RuntimeException("Illegal or statement expected: "
                    + PredicateBuilder.class.getSimpleName() + ", found: " +
                    ((predicate == null) ? "null" : predicate.getClass().getSimpleName()));
        }
        int index = lsPredicates.size() - 2;
        Predicate first = lsPredicates.remove(index);
        Predicate second = lsPredicates.remove(index);
        lsPredicates.add(Predicates.or(first, second));
        return this;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer();
        sb.append("PredicateBuilder");
        sb.append("{\n");
        sb.append(lsPredicates.size() == 0 ? "" : lsPredicates.get(0));
        sb.append("\n}");
        return sb.toString();
    }
}

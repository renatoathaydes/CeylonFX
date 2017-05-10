/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
package com.sun.webkit.text;

import java.text.BreakIterator;
import java.text.CharacterIterator;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

final class TextBreakIterator {

    // Break iterator types
    static final int CHARACTER_ITERATOR = 0;
    static final int WORD_ITERATOR = 1;
    static final int LINE_ITERATOR = 2;
    static final int SENTENCE_ITERATOR = 3;

    // Break iterator methods
    static final int TEXT_BREAK_FIRST = 0;
    static final int TEXT_BREAK_LAST = 1;
    static final int TEXT_BREAK_NEXT = 2;
    static final int TEXT_BREAK_PREVIOUS = 3;
    static final int TEXT_BREAK_CURRENT = 4;
    static final int TEXT_BREAK_PRECEDING = 5;
    static final int TEXT_BREAK_FOLLOWING = 6;
    static final int IS_TEXT_BREAK = 7;
    static final int IS_WORD_TEXT_BREAK = 8;

    // The cache key is the combination of iterator type and locale name.
    private final static class CacheKey {
        private final int type;
        private final Locale locale;
        private final int hashCode;

        CacheKey(int type, Locale locale) {
            this.type = type;
            this.locale = locale;
            this.hashCode = locale.hashCode() + type;
        }

        public boolean equals(Object o){
            if (!(o instanceof CacheKey)) {
                return false;
            }
            CacheKey that = (CacheKey) o;
            return (that.type == this.type) && that.locale.equals(this.locale);
        }

        public int hashCode() {
            return hashCode;
        }
    }

    private final static Map<CacheKey, BreakIterator> iteratorCache =
        //new WeakHashMap<CacheKey, BreakIterator>();
        new HashMap<CacheKey, BreakIterator>();

    static BreakIterator getIterator(
        int type,
        String localeName,
        String text,
        boolean create)
    {
        String[] parts = localeName.split("-");
        String country;
        switch (parts.length) {
            case 1: country = null; break;
            case 2: country = parts[1]; break;
            default: country = parts[2]; break;
        }
        String lang = parts[0].toLowerCase();
        Locale locale = (country == null ?
            new Locale(lang) : new Locale(lang, country.toUpperCase()));

        BreakIterator iterator;
        if (create) {
            iterator = createIterator(type, locale);
        } else {
            CacheKey key = new CacheKey(type, locale);
            iterator = iteratorCache.get(key);
            if (iterator == null) {
                iterator = createIterator(type, locale);
                iteratorCache.put(key, iterator);
            }
        }
        iterator.setText(text);
        return iterator;
    }

    private static BreakIterator createIterator(int type, Locale locale) {
        switch (type) {
        case CHARACTER_ITERATOR:
            return BreakIterator.getCharacterInstance(locale);
        case WORD_ITERATOR:
            return BreakIterator.getWordInstance(locale);
        case LINE_ITERATOR:
            return BreakIterator.getLineInstance(locale);
        case SENTENCE_ITERATOR:
            return BreakIterator.getSentenceInstance(locale);
        default:
            throw new IllegalArgumentException("invalid type: " + type);
        }
    }

    static int invokeMethod(BreakIterator iterator, int method, int pos) {
        // Sometimes an invalid position is passed in, need to adjust it
        // (otherwise BreakIterator methods throw an exception.)
        CharacterIterator text = iterator.getText();
        int length = text.getEndIndex() - text.getBeginIndex();
        if (method == TEXT_BREAK_PRECEDING && pos > length) {
            // RT-15138
            return length;
        }
        if ((pos < 0) || (pos > length)) {
            pos = (pos < 0 ? 0 : length);
        }

        switch (method) {
        case TEXT_BREAK_FIRST: return iterator.first();
        case TEXT_BREAK_LAST: return iterator.last();
        case TEXT_BREAK_NEXT: return iterator.next();
        case TEXT_BREAK_PREVIOUS: return iterator.previous();
        case TEXT_BREAK_CURRENT: return iterator.current();
        case TEXT_BREAK_PRECEDING: return iterator.preceding(pos);
        case TEXT_BREAK_FOLLOWING: return iterator.following(pos);
        case IS_TEXT_BREAK: return (iterator.isBoundary(pos) ? 1 : 0);
        case IS_WORD_TEXT_BREAK: return 1;
        default:
            throw new IllegalArgumentException("invalid method: " + method);
        }
    }
}

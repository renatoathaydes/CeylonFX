/*
 * Copyright (c) 2011, 2013, Oracle and/or its affiliates. All rights reserved.
 */
#ifndef ClipboardUtilitiesJava_h
#define ClipboardUtilitiesJava_h

#include <wtf/Forward.h>

namespace WebCore {

#if OS(WINDOWS)
void replaceNewlinesWithWindowsStyleNewlines(String&);
#endif
void replaceNBSPWithSpace(String&);

} // namespace WebCore

#endif // ClipboardUtilitiesJava_h

# ERRORS

## [ERR-20260314-001] maven-fastjson2-version

**Logged**: 2026-03-14T23:32:00+08:00
**Priority**: high
**Status**: resolved
**Area**: backend

### Summary
Maven build failed because `com.alibaba.fastjson2:fastjson2:2.0.47` could not be resolved from Maven Central.

### Error
```
Could not find artifact com.alibaba:fastjson2:jar:2.0.47 in central
```

### Context
- Command: `mvn -q -DskipTests package`
- Project: `tcm-prescription`
- Root cause: pinned dependency version was unavailable in current Maven Central resolution path.

### Suggested Fix
Upgrade to an available Central version, e.g. `2.0.57`.

### Metadata
- Reproducible: yes
- Related Files: pom.xml

### Resolution
- **Resolved**: 2026-03-14T23:36:00+08:00
- **Notes**: Updated fastjson2 dependency to 2.0.57 and corrected Maven groupId from `com.alibaba` to `com.alibaba.fastjson2`.

---

## [ERR-20260314-002] kimi-bulk-herb-timeout

**Logged**: 2026-03-14T23:47:00+08:00
**Priority**: high
**Status**: resolved
**Area**: backend

### Summary
Kimi herb initialization timed out when requesting about 50 herbs in a single completion.

### Error
```
java.net.SocketTimeoutException: timeout
```

### Context
- Endpoint: `POST /api/herbs/init`
- Cause: single oversized prompt for bulk herb generation.

### Suggested Fix
Split herb initialization into small batches (e.g. 10 herbs per request).

### Metadata
- Reproducible: yes
- Related Files: src/main/java/com/tcm/prescription/controller/HerbController.java

### Resolution
- **Resolved**: 2026-03-14T23:48:00+08:00
- **Notes**: Changed herb initialization to batch requests in groups of 10.

---


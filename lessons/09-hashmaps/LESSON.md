# Lesson 9: HashMap, the counting machine

**Files to edit:** `Frequency.java` (in `lessons/09-hashmaps/`)

## What is a map?

A `List` finds things by position: "give me item number 3". A **map** finds
things by name: "give me the value stored under this **key**". Think of a
dictionary (word -> definition) or a phone's contacts (name -> number).

```java
import java.util.HashMap;
import java.util.Map;

Map<String, Integer> jersey = new HashMap<>();   // key type, value type
jersey.put("Maya", 7);          // store 7 under the key "Maya"
jersey.put("Leo", 23);
jersey.put("Maya", 10);         // same key again: REPLACES the 7

jersey.get("Leo");              // 23
jersey.get("Zed");              // null  (no such key!)
jersey.containsKey("Zed");      // false
jersey.getOrDefault("Zed", 0);  // 0     (a safe get: you pick the fallback)
jersey.size();                  // 2
```

Looping over everything in a map:

```java
for (String name : jersey.keySet()) {
    System.out.println(name + " wears " + jersey.get(name));
}
```

Two things to know about HashMap:

- It is FAST. Finding a key takes about the same time whether the map holds
  ten entries or ten million. (How? It turns the key into a number, the
  "hash", and uses that to jump straight to the right spot. Hence the name.)
- It has NO ORDER. When you loop over `keySet()`, the keys come out in
  whatever order the map likes. Never rely on it.

## Counting with a map

The classic job for a map is a tally: key = the thing, value = how many
times you have seen it. Imagine tallying votes on a whiteboard. For each
ballot you either add a new name with one mark, or add one more mark beside
a name that is already there. A map is that whiteboard.

## Your job

Open `lessons/09-hashmaps/Frequency.java`.

1. `countOccurrences(numbers)`: returns a map from each number to how many
   times it appears. `[1, 2, 2, 3, 2, 1]` gives `{1=2, 2=3, 3=1}`.
2. `mostFrequent(numbers)`: the number that appears most often. Build the
   tally with `countOccurrences`, then search the map for the biggest count
   (the "best so far" pattern from lesson 8). The list is never empty and
   there is never a tie.
3. `countPrefixes(words, size)`: a **prefix** is the start of a word. The
   prefix of size 3 of "robot" is "rob". Return a map from each prefix of
   that size to how many words start with it. Words that are shorter than
   `size` have no such prefix: skip them.
   `["robot", "robe", "rocket"]` with size 3 gives `{rob=2, roc=1}`.
4. `topPrefixes(words, size, howMany)`: the most common prefixes, most
   common first. `howMany` says how many to return (3 means "top three").
   - If two prefixes have the same count, the one that comes first
     alphabetically wins.
   - If there are fewer than `howMany` different prefixes, return them all.

   This one takes real thought, and that's the point. One plan: find the
   single best prefix in the tally, add it to your answer, remove it from
   the tally, and repeat. Work a small example on paper before you type.

Heads-up when comparing: for two `int` values use `==`, but for Strings and
`Integer` objects use `.equals(...)` to ask "same value?", and
`a.compareTo(b)` to ask "which comes first?". You met `compareTo` in lesson 6.

## Look it up

- HashMap:
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/HashMap.html
- Map (see `getOrDefault`, `containsKey`, `keySet`, `remove`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/util/Map.html
- String (see `substring`, `compareTo`):
  https://docs.oracle.com/en/java/javase/25/docs/api/java.base/java/lang/String.html

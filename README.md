# Profanity

Obfuscate, filter, mute or block chat messages with bad words.

Profanity is a lightweight Hytale mod that enforces chat hygiene by detecting and handling offensive or unwanted language in real time.

What it does is simple: match words you do not want, and apply predictable, configurable consequences.  
This mod is designed for servers that want deterministic behavior, low overhead, and full control over how chat moderation is enforced.

This mod comes with a blacklist of words based on [LDNOOBW](https://github.com/LDNOOBW/List-of-Dirty-Naughty-Obscene-and-Otherwise-Bad-Words).

## Configuration

**Action**: possible values are "obfuscate" and "filter"  
Obfuscate will replace bad words with "***", filter will stop the entire message from being sent.

**FilterResponse**: Response message to the player when Action is set to "filter".

**Whitelist**: Phrases that are in the default blacklist but should be allowed.

**Blacklist**: List of disallowed phrases that will be APPENDED to the default blacklist.

**Languages**: Specify which language word lists to load from the included LDNOOBW data.

### Default configuration

```json
{
  "Action": "filter",
  "FilterResponse": "Your message was blocked due to offensive language.",
  "Whitelist": [],
  "Blacklist": [],
  "Languages": ["ar", "zh", "cs", "da", "nl", "en", "eo", "fil", "fi", "fr", "de", "hi", "hu", "it", "ja", "kab", "tlh", "ko", "no", "fa", "pl", "pt", "ru", "es", "sv", "th", "tr"]
}
```

## Supported languages

| Name            | Code            |
| --------------- | --------------- |
| Arabic          | ar              |
| Chinese         | zh              |
| Czech           | cs              |
| Danish          | da              |
| Dutch           | nl              |
| English         | en              |
| Esperanto       | eo              |
| Filipino        | fil             |
| Finnish         | fi              |
| French          | fr              |
| German          | de              |
| Hindi           | hi              |
| Hungarian       | hu              |
| Italian         | it              |
| Japanese        | ja              |
| Kabyle          | kab             |
| Klingon         | tlh             |
| Korean          | ko              |
| Norwegian       | no              |
| Persian         | fa              |
| Polish          | pl              |
| Portuguese      | pt              |
| Russian         | ru              |
| Spanish         | es              |
| Swedish         | sv              |
| Thai            | th              |
| Turkish         | tr              |

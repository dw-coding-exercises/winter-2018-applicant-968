# Election finder

# Improvements

 + Post to the same address that loads the search form, so an annotated form can
   be shown at the same address for an error response. Successful search returns
   a 302 pointing to the relevant information page, perhaps a page like

      /search?ocrids=...

 + Display the dates in the user's timezone as determined on the client
 + Use
   [OCR-ID mappings](https://github.com/opencivicdata/ocd-division-ids/tree/master/identifiers/country-us)
   to display district divisions in a more user-friendly way
 + Augment place data to with information from another service (possibly the
   [Open Civic Data API](https://github.com/opencivicdata/imago) or
   https://smartystreets.com) for more precise OCD-IDs

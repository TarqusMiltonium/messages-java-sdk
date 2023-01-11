# Notes on Unit/Integration Tests

This Test suite is currently nowhere near exhaustive for this library.

This is an initial test to try out the most basic functions, as there was no test code prior to this.
This suite should be added-to before and after any changes are made to this API implementation library.

## Testing using Live Credentials

Tests can be configured to execute using credentials against a live API server. This is currently optimised for a
local-development environment; a CI setup can be configured to use this (or some other alternative) at some future point in time.

To enable use of Tests that use credentials, simply copy the file:
`src/test/resources/testConfig/credentials.dev.local.sample.properties`
to:
`src/test/resources/testConfig/credentials.dev.local.properties`
and update the properties within that copy.

This will allow Tests to use credentials within that file.

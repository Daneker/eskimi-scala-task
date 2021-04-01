# eskimi-scala-task

This is a real-time bidding agent using Scala and Akka toolkit.
A real-time bidding agent is a simple HTTP server that accepts JSON requests, does some matching between advertising campaigns and the received bid request and responds with either a JSON response with a matched campaign (bid) or an empty response (no bid).

### campaign matching logic
1. Filter out the campaign list 
      a. if they contain bid's siteID
      
      b. if bid's device or user country equals the campaign country
      
      c. if campaign has suitable banners for bid's impressions -> match banners and impressions by w and h
      
2. If after filtering there is still more than 1 campaign left, prioritize by device geo, then by user geo (reduce campaign list)

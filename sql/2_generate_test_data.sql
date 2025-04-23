-- Script for generating test data for inventory table
WITH RECURSIVE 
  numbers(n) AS (
    SELECT 1
    UNION ALL
    SELECT n + 1 FROM numbers WHERE n < 1000
  ),
  locations AS (
    SELECT 'London' as loc UNION ALL
    SELECT 'Manchester' UNION ALL
    SELECT 'Birmingham' UNION ALL
    SELECT 'Glasgow' UNION ALL
    SELECT 'Liverpool' UNION ALL
    SELECT 'Bristol' UNION ALL
    SELECT 'Leeds' UNION ALL
    SELECT 'Sheffield' UNION ALL
    SELECT 'Edinburgh' UNION ALL
    SELECT 'Cardiff' UNION ALL
    SELECT 'Newcastle upon Tyne' UNION ALL
    SELECT 'Belfast' UNION ALL
    SELECT 'Leicester' UNION ALL
    SELECT 'Nottingham' UNION ALL
    SELECT 'Southampton' UNION ALL
    SELECT 'Aberdeen' UNION ALL
    SELECT 'Portsmouth' UNION ALL
    SELECT 'Brighton' UNION ALL
    SELECT 'Plymouth' UNION ALL
    SELECT 'Reading' UNION ALL
    SELECT 'Middlesbrough' UNION ALL
    SELECT 'Derby' UNION ALL
    SELECT 'Norwich' UNION ALL
    SELECT 'Swansea' UNION ALL
    SELECT 'York' UNION ALL
    SELECT 'Cambridge' UNION ALL
    SELECT 'Oxford' UNION ALL
    SELECT 'Exeter' UNION ALL
    SELECT 'Bath' UNION ALL
    SELECT 'Chester' UNION ALL
    SELECT 'Canterbury' UNION ALL
    SELECT 'Inverness' UNION ALL
    SELECT 'Dundee' UNION ALL
    SELECT 'Stoke-on-Trent' UNION ALL
    SELECT 'Coventry' UNION ALL
    SELECT 'Wolverhampton' UNION ALL
    SELECT 'Sunderland' UNION ALL
    SELECT 'Bradford' UNION ALL
    SELECT 'Hull' UNION ALL
    SELECT 'Luton' UNION ALL
    SELECT 'Milton Keynes'
  ),
  items AS (
    -- Laptops
    SELECT 'Dell XPS 13 Laptop' as name UNION ALL
    SELECT 'MacBook Pro 14"' UNION ALL
    SELECT 'Lenovo ThinkPad X1' UNION ALL
    SELECT 'HP Spectre x360' UNION ALL
    SELECT 'ASUS ROG Zephyrus' UNION ALL
    -- Monitors
    SELECT 'Dell 27" 4K Monitor' UNION ALL
    SELECT 'LG 32" UltraGear' UNION ALL
    SELECT 'Samsung 34" Curved' UNION ALL
    SELECT 'BenQ 27" Designer' UNION ALL
    SELECT 'AOC 24" Gaming' UNION ALL
    -- Keyboards
    SELECT 'Logitech MX Keyboard' UNION ALL
    SELECT 'Corsair K95 RGB' UNION ALL
    SELECT 'Ducky One 2' UNION ALL
    SELECT 'Keychron K8' UNION ALL
    SELECT 'Microsoft Sculpt' UNION ALL
    -- Mice
    SELECT 'Logitech MX Master' UNION ALL
    SELECT 'Razer DeathAdder' UNION ALL
    SELECT 'SteelSeries Prime' UNION ALL
    SELECT 'Glorious Model O' UNION ALL
    SELECT 'Apple Magic Mouse' UNION ALL
    -- Printers
    SELECT 'HP LaserJet Pro' UNION ALL
    SELECT 'Canon PIXMA' UNION ALL
    SELECT 'Brother MFC-L8900' UNION ALL
    SELECT 'Epson WorkForce' UNION ALL
    SELECT 'Xerox WorkCentre' UNION ALL
    -- Scanners
    SELECT 'Epson V39 Scanner' UNION ALL
    SELECT 'Canon DR-C230' UNION ALL
    SELECT 'Fujitsu ScanSnap' UNION ALL
    SELECT 'Brother ADS-2700W' UNION ALL
    SELECT 'Plustek eScan' UNION ALL
    -- Storage
    SELECT 'Samsung 1TB External SSD' UNION ALL
    SELECT 'WD My Book 4TB' UNION ALL
    SELECT 'Seagate Backup Plus' UNION ALL
    SELECT 'SanDisk Extreme Pro' UNION ALL
    SELECT 'LaCie Rugged' UNION ALL
    -- Networking
    SELECT 'Cisco Switch 24-Port' UNION ALL
    SELECT 'Ubiquiti UniFi AP' UNION ALL
    SELECT 'Netgear Nighthawk' UNION ALL
    SELECT 'TP-Link Omada' UNION ALL
    SELECT 'Aruba Instant On' UNION ALL
    -- Audio/Video
    SELECT 'Sony Wireless Headset' UNION ALL
    SELECT 'Jabra Speak 710' UNION ALL
    SELECT 'Logitech Brio 4K' UNION ALL
    SELECT 'Blue Yeti X' UNION ALL
    SELECT 'Sennheiser HD 660S' UNION ALL
    -- Accessories
    SELECT 'USB-C Hub' UNION ALL
    SELECT 'Anker PowerCore' UNION ALL
    SELECT 'Belkin Thunderbolt 3' UNION ALL
    SELECT 'Logitech StreamCam' UNION ALL
    SELECT 'Elgato Stream Deck'
  )
INSERT INTO inventory (item_name, item_quantity, item_location)
SELECT 
  i.name,
  ABS(RANDOM() % 100) + 1 as item_quantity,
  l.loc
FROM numbers n
CROSS JOIN (
  SELECT name, ROW_NUMBER() OVER (ORDER BY RANDOM()) as rn 
  FROM items
) i
CROSS JOIN (
  SELECT loc, ROW_NUMBER() OVER (ORDER BY RANDOM()) as rn 
  FROM locations
) l
WHERE i.rn = ((n - 1) % 50) + 1
AND l.rn = ((n - 1) % 41) + 1; 
const data1 = {
  "nodes": [
    {
      "data": {
        "id": "36",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 10
      },
      "position": {
        "x": 8240,
        "y": 5030
      }
    },
    {
      "data": {
        "id": "33",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 9
      },
      "position": {
        "x": 7820,
        "y": 4950
      }
    },
    {
      "data": {
        "id": "35",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 9
      },
      "position": {
        "x": 7550,
        "y": 5020
      }
    },
    {
      "data": {
        "id": "32",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 6
      },
      "position": {
        "x": 8620,
        "y": 4520
      }
    },
    {
      "data": {
        "id": "40",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 12,
        "incomingFlow": 0
      },
      "position": {
        "x": 8680,
        "y": 4320
      }
    },
    {
      "data": {
        "id": "45",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 25,
        "incomingFlow": 0
      },
      "position": {
        "x": 7670,
        "y": 5240
      }
    },
    {
      "data": {
        "id": "37",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 10
      },
      "position": {
        "x": 8050,
        "y": 4990
      }
    },
    {
      "data": {
        "id": "41",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 15,
        "incomingFlow": 4
      },
      "position": {
        "x": 8780,
        "y": 4760
      }
    },
    {
      "data": {
        "id": "34",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 5
      },
      "position": {
        "x": 6860,
        "y": 4780
      }
    },
    {
      "data": {
        "id": "44",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 4,
        "incomingFlow": 0
      },
      "position": {
        "x": 6650,
        "y": 4890
      }
    },
    {
      "data": {
        "id": "31",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 2
      },
      "position": {
        "x": 8210,
        "y": 4860
      }
    },
    {
      "data": {
        "id": "38",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 12,
        "incomingFlow": 0
      },
      "position": {
        "x": 8300,
        "y": 5270
      }
    },
    {
      "data": {
        "id": "30",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 2
      },
      "position": {
        "x": 8460,
        "y": 4810
      }
    },
    {
      "data": {
        "id": "39",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 12,
        "incomingFlow": 0
      },
      "position": {
        "x": 8620,
        "y": 4970
      }
    },
    {
      "data": {
        "id": "42",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 8,
        "incomingFlow": 5
      },
      "position": {
        "x": 7020,
        "y": 4650
      }
    },
    {
      "data": {
        "id": "47",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 10,
        "incomingFlow": 0
      },
      "position": {
        "x": 7670,
        "y": 4730
      }
    },
    {
      "data": {
        "id": "46",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 14,
        "incomingFlow": 0
      },
      "position": {
        "x": 8100,
        "y": 5190
      }
    },
    {
      "data": {
        "id": "43",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 24,
        "incomingFlow": 9
      },
      "position": {
        "x": 7310,
        "y": 5050
      }
    }
  ],
  "edges": [
    {
      "data": {
        "label": "10/14",
        "source": "36",
        "target": "37",
        "currentFlow": 10,
        "repairCost": 9
      }
    },
    {
      "data": {
        "label": "5/5",
        "source": "33",
        "target": "34",
        "currentFlow": 5,
        "repairCost": 19
      }
    },
    {
      "data": {
        "label": "9/13",
        "source": "33",
        "target": "35",
        "currentFlow": 9,
        "repairCost": 21
      }
    },
    {
      "data": {
        "label": "9/9",
        "source": "35",
        "target": "43",
        "currentFlow": 9,
        "repairCost": 14
      }
    },
    {
      "data": {
        "label": "2/14",
        "source": "32",
        "target": "31",
        "currentFlow": 2,
        "repairCost": 12
      }
    },
    {
      "data": {
        "label": "4/4",
        "source": "32",
        "target": "41",
        "currentFlow": 4,
        "repairCost": 22
      }
    },
    {
      "data": {
        "label": "6/6",
        "source": "40",
        "target": "32",
        "currentFlow": 6,
        "repairCost": 22
      }
    },
    {
      "data": {
        "label": "0/17",
        "source": "45",
        "target": "35",
        "currentFlow": 0,
        "repairCost": 26
      }
    },
    {
      "data": {
        "label": "9/9",
        "source": "37",
        "target": "33",
        "currentFlow": 9,
        "repairCost": 20
      }
    },
    {
      "data": {
        "label": "1/1",
        "source": "37",
        "target": "31",
        "currentFlow": 1,
        "repairCost": 22
      }
    },
    {
      "data": {
        "label": "5/13",
        "source": "34",
        "target": "42",
        "currentFlow": 5,
        "repairCost": 6
      }
    },
    {
      "data": {
        "label": "0/6",
        "source": "44",
        "target": "34",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "5/5",
        "source": "31",
        "target": "33",
        "currentFlow": 5,
        "repairCost": 22
      }
    },
    {
      "data": {
        "label": "10/18",
        "source": "38",
        "target": "36",
        "currentFlow": 10,
        "repairCost": 6
      }
    },
    {
      "data": {
        "label": "2/7",
        "source": "30",
        "target": "31",
        "currentFlow": 2,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "2/17",
        "source": "39",
        "target": "30",
        "currentFlow": 2,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "0/17",
        "source": "47",
        "target": "33",
        "currentFlow": 0,
        "repairCost": 12
      }
    },
    {
      "data": {
        "label": "0/7",
        "source": "46",
        "target": "36",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "40",
        "target": "39",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "39",
        "target": "38",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "38",
        "target": "40",
        "currentFlow": 0,
        "repairCost": 0
      }
    }
  ],
  "maxFlow": 18,
  "roadsRepairCost": 195
};
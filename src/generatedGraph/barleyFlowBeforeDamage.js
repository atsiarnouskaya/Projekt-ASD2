const data1 = {
  "nodes": [
    {
      "data": {
        "id": "0",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 8
      },
      "position": {
        "x": 220,
        "y": 4380
      }
    },
    {
      "data": {
        "id": "5",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 8,
        "incomingFlow": 0
      },
      "position": {
        "x": 1020,
        "y": 4570
      }
    },
    {
      "data": {
        "id": "1",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 7
      },
      "position": {
        "x": 830,
        "y": 4380
      }
    },
    {
      "data": {
        "id": "3",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 7
      },
      "position": {
        "x": 1080,
        "y": 3720
      }
    },
    {
      "data": {
        "id": "10",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 20,
        "incomingFlow": 8
      },
      "position": {
        "x": 10,
        "y": 4320
      }
    },
    {
      "data": {
        "id": "4",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 1
      },
      "position": {
        "x": 700,
        "y": 4730
      }
    },
    {
      "data": {
        "id": "11",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 6,
        "incomingFlow": 1
      },
      "position": {
        "x": 550,
        "y": 4900
      }
    },
    {
      "data": {
        "id": "15",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 17,
        "incomingFlow": 0
      },
      "position": {
        "x": 860,
        "y": 3610
      }
    },
    {
      "data": {
        "id": "2",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 0
      },
      "position": {
        "x": 1620,
        "y": 4380
      }
    },
    {
      "data": {
        "id": "9",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 8,
        "incomingFlow": 0
      },
      "position": {
        "x": 890,
        "y": 4930
      }
    },
    {
      "data": {
        "id": "8",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 8,
        "incomingFlow": 0
      },
      "position": {
        "x": 1820,
        "y": 4270
      }
    },
    {
      "data": {
        "id": "12",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 28,
        "incomingFlow": 13
      },
      "position": {
        "x": 840,
        "y": 3940
      }
    },
    {
      "data": {
        "id": "6",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 8,
        "incomingFlow": 0
      },
      "position": {
        "x": 1320,
        "y": 3470
      }
    },
    {
      "data": {
        "id": "7",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 8,
        "incomingFlow": 0
      },
      "position": {
        "x": 420,
        "y": 4490
      }
    },
    {
      "data": {
        "id": "13",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 8,
        "incomingFlow": 0
      },
      "position": {
        "x": 380,
        "y": 4130
      }
    },
    {
      "data": {
        "id": "16",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 7,
        "incomingFlow": 0
      },
      "position": {
        "x": 650,
        "y": 4130
      }
    },
    {
      "data": {
        "id": "14",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 25,
        "incomingFlow": 0
      },
      "position": {
        "x": 1630,
        "y": 4170
      }
    }
  ],
  "edges": [
    {
      "data": {
        "label": "8/17",
        "source": "0",
        "target": "10",
        "currentFlow": 8,
        "repairCost": 26
      }
    },
    {
      "data": {
        "label": "7/7",
        "source": "5",
        "target": "1",
        "currentFlow": 7,
        "repairCost": 20
      }
    },
    {
      "data": {
        "label": "0/17",
        "source": "1",
        "target": "0",
        "currentFlow": 0,
        "repairCost": 1
      }
    },
    {
      "data": {
        "label": "7/7",
        "source": "1",
        "target": "3",
        "currentFlow": 7,
        "repairCost": 15
      }
    },
    {
      "data": {
        "label": "13/16",
        "source": "3",
        "target": "12",
        "currentFlow": 13,
        "repairCost": 7
      }
    },
    {
      "data": {
        "label": "0/1",
        "source": "4",
        "target": "1",
        "currentFlow": 0,
        "repairCost": 4
      }
    },
    {
      "data": {
        "label": "1/11",
        "source": "4",
        "target": "11",
        "currentFlow": 1,
        "repairCost": 17
      }
    },
    {
      "data": {
        "label": "0/15",
        "source": "15",
        "target": "3",
        "currentFlow": 0,
        "repairCost": 5
      }
    },
    {
      "data": {
        "label": "0/0",
        "source": "2",
        "target": "1",
        "currentFlow": 0,
        "repairCost": 4
      }
    },
    {
      "data": {
        "label": "1/1",
        "source": "9",
        "target": "4",
        "currentFlow": 1,
        "repairCost": 1
      }
    },
    {
      "data": {
        "label": "0/9",
        "source": "8",
        "target": "2",
        "currentFlow": 0,
        "repairCost": 20
      }
    },
    {
      "data": {
        "label": "6/6",
        "source": "6",
        "target": "3",
        "currentFlow": 6,
        "repairCost": 10
      }
    },
    {
      "data": {
        "label": "8/16",
        "source": "7",
        "target": "0",
        "currentFlow": 8,
        "repairCost": 23
      }
    },
    {
      "data": {
        "label": "0/7",
        "source": "13",
        "target": "0",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "0/1",
        "source": "16",
        "target": "1",
        "currentFlow": 0,
        "repairCost": 6
      }
    },
    {
      "data": {
        "label": "0/1",
        "source": "14",
        "target": "2",
        "currentFlow": 0,
        "repairCost": 22
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "6",
        "target": "8",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "8",
        "target": "9",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "9",
        "target": "7",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "7",
        "target": "6",
        "currentFlow": 0,
        "repairCost": 0
      }
    }
  ],
  "maxFlow": 22,
  "roadsRepairCost": 119
};
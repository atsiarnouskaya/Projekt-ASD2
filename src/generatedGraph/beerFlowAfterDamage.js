const data4 = {
  "nodes": [
    {
      "data": {
        "id": "0",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 2
      },
      "position": {
        "x": 830,
        "y": 4380
      }
    },
    {
      "data": {
        "id": "8",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 5,
        "incomingFlow": 0
      },
      "position": {
        "x": 1860,
        "y": 4130
      }
    },
    {
      "data": {
        "id": "15",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 11,
        "incomingFlow": 0
      },
      "position": {
        "x": 990,
        "y": 4260
      }
    },
    {
      "data": {
        "id": "2",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 2
      },
      "position": {
        "x": 1080,
        "y": 3720
      }
    },
    {
      "data": {
        "id": "10",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 5,
        "incomingFlow": 0
      },
      "position": {
        "x": 560,
        "y": 4840
      }
    },
    {
      "data": {
        "id": "14",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 15,
        "incomingFlow": 0
      },
      "position": {
        "x": 110,
        "y": 4720
      }
    },
    {
      "data": {
        "id": "4",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 0
      },
      "position": {
        "x": 400,
        "y": 4060
      }
    },
    {
      "data": {
        "id": "12",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 2,
        "incomingFlow": 0
      },
      "position": {
        "x": 1120,
        "y": 3510
      }
    },
    {
      "data": {
        "id": "9",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 5,
        "incomingFlow": 0
      },
      "position": {
        "x": 460,
        "y": 3820
      }
    },
    {
      "data": {
        "id": "5",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 0
      },
      "position": {
        "x": 370,
        "y": 4380
      }
    },
    {
      "data": {
        "id": "6",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 0
      },
      "position": {
        "x": 350,
        "y": 4680
      }
    },
    {
      "data": {
        "id": "7",
        "type": "Farmland",
        "label": "F",
        "productionCapacity": 5,
        "incomingFlow": 0
      },
      "position": {
        "x": 510,
        "y": 4200
      }
    },
    {
      "data": {
        "id": "3",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 2
      },
      "position": {
        "x": 700,
        "y": 4730
      }
    },
    {
      "data": {
        "id": "1",
        "type": "Intersection",
        "label": "I",
        "productionCapacity": 0,
        "incomingFlow": 2
      },
      "position": {
        "x": 1620,
        "y": 4380
      }
    },
    {
      "data": {
        "id": "11",
        "type": "Brewery",
        "label": "B",
        "productionCapacity": 2,
        "incomingFlow": 0
      },
      "position": {
        "x": 1770,
        "y": 4550
      }
    },
    {
      "data": {
        "id": "16",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 12,
        "incomingFlow": 2
      },
      "position": {
        "x": 1610,
        "y": 4130
      }
    },
    {
      "data": {
        "id": "13",
        "type": "Tavern",
        "label": "T",
        "productionCapacity": 27,
        "incomingFlow": 2
      },
      "position": {
        "x": 500,
        "y": 4640
      }
    }
  ],
  "edges": [
    {
      "data": {
        "label": "2/3",
        "source": "0",
        "target": "3",
        "currentFlow": 2,
        "repairCost": 3
      }
    },
    {
      "data": {
        "label": "0/6",
        "source": "8",
        "target": "1",
        "currentFlow": 0,
        "repairCost": 20
      }
    },
    {
      "data": {
        "label": "0/12",
        "source": "15",
        "target": "0",
        "currentFlow": 0,
        "repairCost": 24
      }
    },
    {
      "data": {
        "label": "2/14",
        "source": "2",
        "target": "0",
        "currentFlow": 2,
        "repairCost": 1
      }
    },
    {
      "data": {
        "label": "0/7",
        "source": "10",
        "target": "6",
        "currentFlow": 0,
        "repairCost": 7
      }
    },
    {
      "data": {
        "label": "0/16",
        "source": "14",
        "target": "6",
        "currentFlow": 0,
        "repairCost": 22
      }
    },
    {
      "data": {
        "label": "2/16",
        "source": "12",
        "target": "2",
        "currentFlow": 2,
        "repairCost": 14
      }
    },
    {
      "data": {
        "label": "0/3",
        "source": "9",
        "target": "4",
        "currentFlow": 0,
        "repairCost": 17
      }
    },
    {
      "data": {
        "label": "0/18",
        "source": "5",
        "target": "0",
        "currentFlow": 0,
        "repairCost": 2
      }
    },
    {
      "data": {
        "label": "0/16",
        "source": "5",
        "target": "4",
        "currentFlow": 0,
        "repairCost": 18
      }
    },
    {
      "data": {
        "label": "0/1",
        "source": "6",
        "target": "5",
        "currentFlow": 0,
        "repairCost": 14
      }
    },
    {
      "data": {
        "label": "0/8",
        "source": "7",
        "target": "5",
        "currentFlow": 0,
        "repairCost": 6
      }
    },
    {
      "data": {
        "label": "2/11",
        "source": "3",
        "target": "13",
        "currentFlow": 2,
        "repairCost": 13
      }
    },
    {
      "data": {
        "label": "0/7",
        "source": "1",
        "target": "0",
        "currentFlow": 0,
        "repairCost": 18
      }
    },
    {
      "data": {
        "label": "2/8",
        "source": "1",
        "target": "16",
        "currentFlow": 2,
        "repairCost": 22
      }
    },
    {
      "data": {
        "label": "2/8",
        "source": "11",
        "target": "1",
        "currentFlow": 2,
        "repairCost": 15
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "9",
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
        "target": "10",
        "currentFlow": 0,
        "repairCost": 0
      }
    },
    {
      "data": {
        "label": "P",
        "type": "Polygon",
        "source": "10",
        "target": "9",
        "currentFlow": 0,
        "repairCost": 0
      }
    }
  ],
  "maxFlow": 4,
  "roadsRepairCost": 136
};
<template>
  <div>
    <h1>{{ message }}</h1>
  </div>
</template>

<script>

import mqtt from 'mqtt'
export default {
  data () {
    return {
      message: ''
    }
  },

  created () {
    const options = {
      username: 'admin',
      password: 'admin'
      // clientId: 'test_001' clientId必须唯一，不能有相同的clientId
    }
    this.client = mqtt.connect('ws://192.168.124.5:9001', options)
    this.client.on('connect', () => {
      console.log('Connected to MQTT server')
      this.client.subscribe('test/topic')
    })
  },
  mounted () {
    console.log('调用mounted')
    this.client.on('message', (topic, message) => {
      this.message = message.toString()
      console.log(this.message)
    })
  },

  destroyed () {
    this.client.end()
  }
}
</script>

<template>
    <footer class="page-footer teal blue-grey">
        <div class="container ">
            <div class="row">
                <div class="col l6 s12">
                    <h5 class="white-text">Contacts</h5>
                    <p class="grey-text text-lighten-4">
                        En cas de besoin, vous pouvez toujours me retrouver <br/>sur ces
                        r&eacute;seaux sociaux :
                    </p>
                    <a target="blank" href="https://www.twitter.com/zthulj"
                       class="waves-effect waves-light"><img
                            src="../../../assets/twitter.png" class="icon"/></a> <a target="blank"
                                                                                    href="https://www.linkedin.com/in/jeremythulliez/"
                                                                                    class="waves-effect waves-light"><img
                        src="../../../assets/linkedin.png" class="icon"/></a> <a target="blank"
                                                                                 href="https://github.com/zthulj/"
                                                                                 class="waves-effect waves-light"><img
                        src="../../../assets/github.png" class="icon"/></a>
                </div>
                <div class="col l6 s12">
                    <h5 class="white-text">Liens utiles</h5>
                    <ul>
                        <li><Login v-if="this.access_token == ''"></Login></li>
                    </ul>
                </div>
                <div class="col l3 s12">
                    <h5 class="white-text"></h5>

                </div>
            </div>
        </div>
        <div class="footer-copyright blue-grey darken-3">
            <div class="container">
                Made by Jeremy Thulliez, using <a target="blank"
                                                  class="brown-text text-lighten-3" href="http://materializecss.com">Materialize</a>
            </div>
        </div>
    </footer>
</template>

<script>
  import Login from '@/components/thulj/admin/Login.vue'
  export default {
    name: 'Footer',
    computed: {
      access_token () {
        return this.$store.state.auth.access_token
      }
    },
    components: {
      Login
    }
  }
</script>

<style scoped>
    .icon {
        width: 35px;
        margin-left: 5px;
    }
</style>

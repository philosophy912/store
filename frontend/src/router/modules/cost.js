/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const costRouter = {
  path: '/cost',
  component: Layout,
  redirect: '/table/drag-table',
  name: 'CostTable',
  meta: {
    title: '成本管理',
    icon: 'table'
  },
  children: [
    {
      path: 'material-table',
      component: () => import('@/views/table/material-table'),
      name: 'MaterialTable',
      meta: { title: '原材料管理' }
    },
    {
      path: 'basic-table',
      component: () => import('@/views/table/basic-table'),
      name: 'BasicTable',
      meta: { title: '基础产品管理' }
    },
    {
      path: 'middle-table',
      component: () => import('@/views/table/middle-table'),
      name: 'MiddleTable',
      meta: { title: '中级产品使用' }
    }
    // {
    //   path: 'drag-table',
    //   component: () => import('@/views/table/drag-table'),
    //   name: 'DragTable',
    //   meta: { title: 'Drag Table' }
    // },
    // {
    //   path: 'inline-edit-table',
    //   component: () => import('@/views/table/inline-edit-table'),
    //   name: 'InlineEditTable',
    //   meta: { title: 'Inline Edit' }
    // },
    // {
    //   path: 'complex-table',
    //   component: () => import('@/views/table/complex-table'),
    //   name: 'ComplexTable',
    //   meta: { title: 'Complex Table' }
    // }
  ]
}
export default costRouter

/** When your routing table is too long, you can split it into small modules **/

import Layout from '@/layout'

const tableRouter = {
  path: '/table',
  component: Layout,
  redirect: '/table/complex-table',
  name: 'Table',
  meta: {
    title: '设备管理',
    icon: 'table'
  },
  children: [
    {
      path: 'category-table',
      component: () => import('@/views/table/category-table'),
      name: 'CategoryTable',
      meta: { title: '分类管理' }
    },
    {
      path: 'food-table',
      component: () => import('@/views/table/food-table'),
      name: 'FoodTable',
      meta: { title: '耗材管理' }
    },
    {
      path: 'food-use-table',
      component: () => import('@/views/table/food-use-table'),
      name: 'FoodUseTable',
      meta: { title: '耗材使用' }
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
export default tableRouter
